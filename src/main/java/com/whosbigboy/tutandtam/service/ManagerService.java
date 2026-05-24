package com.whosbigboy.tutandtam.service;

import com.whosbigboy.tutandtam.dto.ManagerDtos;
import com.whosbigboy.tutandtam.entity.EventEntity;
import com.whosbigboy.tutandtam.entity.GroupEntity;
import com.whosbigboy.tutandtam.entity.Tour;
import com.whosbigboy.tutandtam.entity.TourJournal;
import com.whosbigboy.tutandtam.exception.NotFoundException;
import com.whosbigboy.tutandtam.repository.EventRepository;
import com.whosbigboy.tutandtam.repository.GroupRepository;
import com.whosbigboy.tutandtam.repository.TourJournalRepository;
import com.whosbigboy.tutandtam.repository.TourRepository;
import com.whosbigboy.tutandtam.repository.TouristsGroupRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    private final GroupRepository groupRepository;
    private final TourRepository tourRepository;
    private final TouristsGroupRepository touristsGroupRepository;
    private final TourJournalRepository tourJournalRepository;
    private final EventRepository eventRepository;

    public ManagerService(GroupRepository groupRepository,
                          TourRepository tourRepository,
                          TouristsGroupRepository touristsGroupRepository,
                          TourJournalRepository tourJournalRepository,
                          EventRepository eventRepository) {
        this.groupRepository = groupRepository;
        this.tourRepository = tourRepository;
        this.touristsGroupRepository = touristsGroupRepository;
        this.tourJournalRepository = tourJournalRepository;
        this.eventRepository = eventRepository;
    }

    public List<ManagerDtos.CurrentGroupResponse> currentGroups() {
        return groupRepository.findAll().stream().map(group -> {
            Tour tour = group.getTourId() != null ? tourRepository.findById(group.getTourId()).orElse(null) : null;
            int participants = touristsGroupRepository.findByGroupId(group.getId()).size();
            return new ManagerDtos.CurrentGroupResponse(
                    group.getId(),
                    group.getName(),
                    tour != null ? tour.getId() : null,
                    tour != null ? tour.getName() : null,
                    participants);
        }).collect(Collectors.toList());
    }

    public ManagerDtos.TimelineResponse timeline(String tourId) {
        Tour tour = tourRepository.findById(tourId).orElseThrow(() -> new NotFoundException("Тур не найден"));
        List<ManagerDtos.TimelinePoint> points = tourJournalRepository.findByTourIdOrderByStartDateAsc(tourId).stream()
                .map(this::toTimelinePoint)
                .collect(Collectors.toList());
        return new ManagerDtos.TimelineResponse(tour.getId(), tour.getName(), points);
    }

    private ManagerDtos.TimelinePoint toTimelinePoint(TourJournal journal) {
        EventEntity event = eventRepository.findById(journal.getEventId())
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено: " + journal.getEventId()));
        return new ManagerDtos.TimelinePoint(event.getId(), event.getName(), event.getDatetime(), resolveColor(journal));
    }

    private String resolveColor(TourJournal journal) {
        Instant now = Instant.now();
        if (journal.getStatus() != null && journal.getStatus() == 1) {
            return "blue";
        }
        if (journal.getStartDate() != null && journal.getStartDate().isBefore(now)) {
            return "red";
        }
        return "green";
    }
}
