package com.whosbigboy.tutandtam.service;

import com.whosbigboy.tutandtam.dto.ManagerDtos;
import com.whosbigboy.tutandtam.entity.EventEntity;
import com.whosbigboy.tutandtam.entity.Feedback;
import com.whosbigboy.tutandtam.entity.GroupEntity;
import com.whosbigboy.tutandtam.entity.Tour;
import com.whosbigboy.tutandtam.entity.TourJournal;
import com.whosbigboy.tutandtam.exception.NotFoundException;
import com.whosbigboy.tutandtam.repository.EventRepository;
import com.whosbigboy.tutandtam.repository.FeedbackRepository;
import com.whosbigboy.tutandtam.repository.GroupRepository;
import com.whosbigboy.tutandtam.repository.TourJournalRepository;
import com.whosbigboy.tutandtam.repository.TourRepository;
import com.whosbigboy.tutandtam.repository.TouristsGroupRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    private final GroupRepository groupRepository;
    private final TourRepository tourRepository;
    private final TouristsGroupRepository touristsGroupRepository;
    private final TourJournalRepository tourJournalRepository;
    private final EventRepository eventRepository;
    private final FeedbackRepository feedbackRepository;

    public ManagerService(GroupRepository groupRepository,
                          TourRepository tourRepository,
                          TouristsGroupRepository touristsGroupRepository,
                          TourJournalRepository tourJournalRepository,
                          EventRepository eventRepository,
                          FeedbackRepository feedbackRepository) {
        this.groupRepository = groupRepository;
        this.tourRepository = tourRepository;
        this.touristsGroupRepository = touristsGroupRepository;
        this.tourJournalRepository = tourJournalRepository;
        this.eventRepository = eventRepository;
        this.feedbackRepository = feedbackRepository;
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

    public byte[] buildTourReportCsv(String tourId) {
        Tour tour = tourRepository.findById(tourId).orElseThrow(() -> new NotFoundException("Тур не найден"));
        List<TourJournal> journal = tourJournalRepository.findByTourIdOrderByStartDateAsc(tourId);
        StringBuilder csv = new StringBuilder();
        csv.append("Тур:;").append(escapeCsv(tour.getName())).append('\n');
        csv.append("Мероприятие;Дата;Статус\n");
        for (TourJournal item : journal) {
            EventEntity event = eventRepository.findById(item.getEventId()).orElse(null);
            String eventName = event != null ? event.getName() : item.getEventId();
            Instant date = event != null ? event.getDatetime() : item.getStartDate();
            csv.append(escapeCsv(eventName)).append(';')
                    .append(date != null ? date : "")
                    .append(';')
                    .append(resolveColor(item))
                    .append('\n');
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] buildPeriodReportCsv(LocalDate from, LocalDate to, List<String> tourIds) {
        List<Tour> tours = tourIds.stream()
                .map(id -> tourRepository.findById(id).orElseThrow(() -> new NotFoundException("Тур не найден: " + id)))
                .collect(Collectors.toList());
        List<Feedback> feedback = feedbackRepository.findAll();
        StringBuilder csv = new StringBuilder();
        csv.append("Период;").append(from).append(" - ").append(to).append('\n');
        csv.append("Тур;Кол-во мероприятий;Оценка тура\n");
        for (Tour tour : tours) {
            long eventsCount = tourJournalRepository.findByTourIdOrderByStartDateAsc(tour.getId()).size();
            double avgRating = feedback.stream()
                    .filter(item -> tour.getId().equals(item.getTourId()))
                    .mapToInt(Feedback::getRating)
                    .average()
                    .orElse(0.0);
            csv.append(escapeCsv(tour.getName())).append(';')
                    .append(eventsCount).append(';')
                    .append(String.format(Locale.US, "%.2f", avgRating))
                    .append('\n');
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
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

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(";") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
