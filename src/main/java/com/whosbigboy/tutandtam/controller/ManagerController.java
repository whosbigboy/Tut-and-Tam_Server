package com.whosbigboy.tutandtam.controller;

import com.whosbigboy.tutandtam.dto.ManagerDtos;
import com.whosbigboy.tutandtam.service.ManagerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/groups/current")
    public List<ManagerDtos.CurrentGroupResponse> currentGroups() {
        return managerService.currentGroups();
    }

    @GetMapping("/tours/{tourId}/timeline")
    public ManagerDtos.TimelineResponse timeline(@PathVariable String tourId) {
        return managerService.timeline(tourId);
    }
}
