package com.whosbigboy.tutandtam.controller;

import com.whosbigboy.tutandtam.dto.AuthDtos;
import com.whosbigboy.tutandtam.dto.TouristDtos;
import com.whosbigboy.tutandtam.entity.Feedback;
import com.whosbigboy.tutandtam.service.TouristService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/tourists")
@Validated
public class TouristController {

    private final TouristService touristService;

    public TouristController(TouristService touristService) {
        this.touristService = touristService;
    }

    @GetMapping("/{socialId}/group")
    public TouristDtos.GroupInfoResponse myGroup(@PathVariable String socialId) {
        return touristService.getGroupInfo(socialId);
    }

    @GetMapping("/{socialId}/tour")
    public TouristDtos.TourInfoResponse myTour(@PathVariable String socialId) {
        return touristService.getTourInfo(socialId);
    }

    @GetMapping("/{socialId}/guide-contact")
    public TouristDtos.GuideContactResponse guideContact(@PathVariable String socialId) {
        return touristService.getGuideContact(socialId);
    }

    @PostMapping("/{socialId}/support")
    public Map<String, String> support(@PathVariable String socialId, @Valid @RequestBody AuthDtos.SupportRequest request) {
        touristService.sendSupportMessage(socialId, request.message());
        return Map.of("status", "accepted");
    }

    @PostMapping("/{socialId}/feedback")
    public Feedback feedback(@PathVariable String socialId, @Valid @RequestBody AuthDtos.FeedbackRequest request) {
        return touristService.submitFeedback(socialId, request);
    }
}
