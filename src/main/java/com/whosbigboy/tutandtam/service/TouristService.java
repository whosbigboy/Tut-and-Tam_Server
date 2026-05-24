package com.whosbigboy.tutandtam.service;

import com.whosbigboy.tutandtam.dto.AuthDtos;
import com.whosbigboy.tutandtam.dto.TouristDtos;
import com.whosbigboy.tutandtam.entity.EventEntity;
import com.whosbigboy.tutandtam.entity.Feedback;
import com.whosbigboy.tutandtam.entity.GroupEntity;
import com.whosbigboy.tutandtam.entity.Guide;
import com.whosbigboy.tutandtam.entity.Tour;
import com.whosbigboy.tutandtam.entity.TourJournal;
import com.whosbigboy.tutandtam.entity.Tourist;
import com.whosbigboy.tutandtam.entity.TouristsGroup;
import com.whosbigboy.tutandtam.exception.BadRequestException;
import com.whosbigboy.tutandtam.exception.NotFoundException;
import com.whosbigboy.tutandtam.repository.EventRepository;
import com.whosbigboy.tutandtam.repository.FeedbackRepository;
import com.whosbigboy.tutandtam.repository.GroupRepository;
import com.whosbigboy.tutandtam.repository.GuideRepository;
import com.whosbigboy.tutandtam.repository.TourJournalRepository;
import com.whosbigboy.tutandtam.repository.TourRepository;
import com.whosbigboy.tutandtam.repository.TouristRepository;
import com.whosbigboy.tutandtam.repository.TouristsGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TouristService {

    private static final Logger log = LoggerFactory.getLogger(TouristService.class);

    private final TouristRepository touristRepository;
    private final TouristsGroupRepository touristsGroupRepository;
    private final GroupRepository groupRepository;
    private final TourRepository tourRepository;
    private final TourJournalRepository tourJournalRepository;
    private final EventRepository eventRepository;
    private final GuideRepository guideRepository;
    private final FeedbackRepository feedbackRepository;

    public TouristService(TouristRepository touristRepository,
                          TouristsGroupRepository touristsGroupRepository,
                          GroupRepository groupRepository,
                          TourRepository tourRepository,
                          TourJournalRepository tourJournalRepository,
                          EventRepository eventRepository,
                          GuideRepository guideRepository,
                          FeedbackRepository feedbackRepository) {
        this.touristRepository = touristRepository;
        this.touristsGroupRepository = touristsGroupRepository;
        this.groupRepository = groupRepository;
        this.tourRepository = tourRepository;
        this.tourJournalRepository = tourJournalRepository;
        this.eventRepository = eventRepository;
        this.guideRepository = guideRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public TouristDtos.GroupInfoResponse getGroupInfo(String touristSocialId) {
        Tourist tourist = touristRepository.findBySocialId(touristSocialId)
                .orElseThrow(() -> new NotFoundException("Турист не найден"));
        GroupEntity group = resolveGroupByTourist(tourist.getId());
        List<AuthDtos.TouristIdentity> members = touristsGroupRepository.findByGroupId(group.getId()).stream()
                .map(TouristsGroup::getTouristId)
                .map(id -> touristRepository.findById(id).orElse(null))
                .filter(t -> t != null)
                .map(t -> new AuthDtos.TouristIdentity(t.getId(), t.getFio()))
                .collect(Collectors.toList());
        Integer participants = touristsGroupRepository.findByGroupId(group.getId()).stream()
                .findFirst()
                .map(TouristsGroup::getNumberOfParticipants)
                .orElse(members.size());
        return new TouristDtos.GroupInfoResponse(group.getId(), group.getName(), participants, members);
    }

    public TouristDtos.TourInfoResponse getTourInfo(String touristSocialId) {
        Tourist tourist = touristRepository.findBySocialId(touristSocialId)
                .orElseThrow(() -> new NotFoundException("Турист не найден"));
        GroupEntity group = resolveGroupByTourist(tourist.getId());
        Tour tour = tourRepository.findById(group.getTourId())
                .orElseThrow(() -> new NotFoundException("Тур не найден"));

        List<TouristDtos.TourEventStatus> events = tourJournalRepository.findByTourIdOrderByStartDateAsc(tour.getId()).stream()
                .map(journal -> {
                    EventEntity event = eventRepository.findById(journal.getEventId()).orElse(null);
                    return new TouristDtos.TourEventStatus(
                            event != null ? event.getName() : "Мероприятие",
                            event != null ? event.getDatetime() : journal.getStartDate(),
                            journal.getStatus());
                })
                .collect(Collectors.toList());
        return new TouristDtos.TourInfoResponse(tour.getId(), tour.getName(), tour.getDescription(), tour.getActive(), events);
    }

    public TouristDtos.GuideContactResponse getGuideContact(String touristSocialId) {
        Tourist tourist = touristRepository.findBySocialId(touristSocialId)
                .orElseThrow(() -> new NotFoundException("Турист не найден"));
        GroupEntity group = resolveGroupByTourist(tourist.getId());
        Guide guide = guideRepository.findById(group.getGuideId())
                .orElseThrow(() -> new NotFoundException("Гид не найден"));
        return new TouristDtos.GuideContactResponse(guide.getId(), guide.getFio(), guide.getPhoneNumber(), guide.getSocialId());
    }

    public void sendSupportMessage(String touristSocialId, String message) {
        Tourist tourist = touristRepository.findBySocialId(touristSocialId)
                .orElseThrow(() -> new NotFoundException("Турист не найден"));
        GroupEntity group = resolveGroupByTourist(tourist.getId());
        log.info("Support message from tourist socialId={} group={} payload={}", touristSocialId, group.getId(), message);
    }

    public Feedback submitFeedback(String touristSocialId, AuthDtos.FeedbackRequest request) {
        if (request.rating() < 1 || request.rating() > 5) {
            throw new BadRequestException("Оценка должна быть от 1 до 5");
        }
        Tourist tourist = touristRepository.findBySocialId(touristSocialId)
                .orElseThrow(() -> new NotFoundException("Турист не найден"));
        GroupEntity group = resolveGroupByTourist(tourist.getId());
        if (!request.tourId().equals(group.getTourId())) {
            throw new BadRequestException("Турист не привязан к указанному туру");
        }

        TourJournal lastEntry = tourJournalRepository.findFirstByTourIdOrderByFinishDateDesc(request.tourId())
                .orElseThrow(() -> new BadRequestException("Для тура отсутствуют даты завершения"));
        if (lastEntry.getFinishDate() == null || lastEntry.getFinishDate().isAfter(Instant.now())) {
            throw new BadRequestException("Оставить отзыв можно только после завершения тура");
        }

        Feedback feedback = new Feedback();
        feedback.setId(UUID.randomUUID().toString());
        feedback.setTouristId(tourist.getId());
        feedback.setTourId(request.tourId());
        feedback.setRating(request.rating());
        feedback.setText(request.text());
        return feedbackRepository.save(feedback);
    }

    private GroupEntity resolveGroupByTourist(String touristId) {
        TouristsGroup binding = touristsGroupRepository.findFirstByTouristId(touristId)
                .orElseThrow(() -> new NotFoundException("Группа для туриста не найдена"));
        return groupRepository.findById(binding.getGroupId())
                .orElseThrow(() -> new NotFoundException("Группа не найдена"));
    }
}
