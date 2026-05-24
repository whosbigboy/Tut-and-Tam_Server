package com.whosbigboy.tutandtam.service;

import com.whosbigboy.tutandtam.dto.AuthDtos;
import com.whosbigboy.tutandtam.exception.BadRequestException;
import com.whosbigboy.tutandtam.repository.EventRepository;
import com.whosbigboy.tutandtam.repository.FeedbackRepository;
import com.whosbigboy.tutandtam.repository.GroupRepository;
import com.whosbigboy.tutandtam.repository.GuideRepository;
import com.whosbigboy.tutandtam.repository.TourJournalRepository;
import com.whosbigboy.tutandtam.repository.TourRepository;
import com.whosbigboy.tutandtam.repository.TouristRepository;
import com.whosbigboy.tutandtam.repository.TouristsGroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class TouristServiceTest {

    @Mock
    private TouristRepository touristRepository;
    @Mock
    private TouristsGroupRepository touristsGroupRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private TourRepository tourRepository;
    @Mock
    private TourJournalRepository tourJournalRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private GuideRepository guideRepository;
    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private TouristService touristService;

    @Test
    void submitFeedback_rejectsOutOfRangeRating() {
        AuthDtos.FeedbackRequest request = new AuthDtos.FeedbackRequest("tour-1", 6, "great");

        assertThatThrownBy(() -> touristService.submitFeedback("social-1", request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Оценка должна быть от 1 до 5");
    }
}
