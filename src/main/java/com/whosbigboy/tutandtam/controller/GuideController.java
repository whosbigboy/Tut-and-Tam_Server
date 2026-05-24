package com.whosbigboy.tutandtam.controller;

import com.whosbigboy.tutandtam.dto.AuthDtos;
import com.whosbigboy.tutandtam.dto.TouristDtos;
import com.whosbigboy.tutandtam.service.GuideService;
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
@RequestMapping("/api/guides")
@Validated
public class GuideController {

    private final GuideService guideService;

    public GuideController(GuideService guideService) {
        this.guideService = guideService;
    }

    @GetMapping("/{socialId}/group")
    public TouristDtos.GroupInfoResponse myGroup(@PathVariable String socialId) {
        return guideService.getOwnGroup(socialId);
    }

    @PostMapping("/{socialId}/broadcast")
    public Map<String, Object> broadcast(@PathVariable String socialId, @Valid @RequestBody AuthDtos.SupportRequest request) {
        int recipients = guideService.broadcastMessage(socialId, request.message());
        return Map.of("recipients", recipients, "status", "sent");
    }
}
