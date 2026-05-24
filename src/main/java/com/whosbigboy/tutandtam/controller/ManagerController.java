package com.whosbigboy.tutandtam.controller;

import com.whosbigboy.tutandtam.dto.ManagerDtos;
import com.whosbigboy.tutandtam.service.ManagerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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

    @GetMapping("/reports/tour/{tourId}")
    public ResponseEntity<byte[]> tourReport(@PathVariable String tourId) {
        byte[] body = managerService.buildTourReportCsv(tourId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tour-report-" + tourId + ".csv")
                .contentType(new MediaType("text", "csv"))
                .body(body);
    }

    @GetMapping("/reports/period")
    public ResponseEntity<byte[]> periodReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam List<String> tourIds
    ) {
        byte[] body = managerService.buildPeriodReportCsv(from, to, tourIds);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=period-report.csv")
                .contentType(new MediaType("text", "csv"))
                .body(body);
    }
}
