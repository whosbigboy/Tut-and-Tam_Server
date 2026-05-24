package com.whosbigboy.tutandtam.controller;

import com.whosbigboy.tutandtam.dto.AuthDtos;
import com.whosbigboy.tutandtam.service.AuthService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/tourist/contract-check")
    public AuthDtos.ContractAuthResponse checkContract(@Valid @RequestBody AuthDtos.ContractAuthRequest request) {
        return authService.checkContract(request.contractNumber());
    }

    @PostMapping("/guide/login")
    public AuthDtos.LoginResponse guideLogin(@Valid @RequestBody AuthDtos.LoginRequest request) {
        return authService.authenticateGuide(request.email(), request.password());
    }

    @PostMapping("/manager/login")
    public AuthDtos.LoginResponse managerLogin(@Valid @RequestBody AuthDtos.LoginRequest request) {
        return authService.authenticateManager(request.email(), request.password());
    }

    @GetMapping("/tours")
    public List<Map<String, Object>> publicTours() {
        return authService.listPublicTours();
    }
}
