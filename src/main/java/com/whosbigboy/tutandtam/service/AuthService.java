package com.whosbigboy.tutandtam.service;

import com.whosbigboy.tutandtam.dto.AuthDtos;
import com.whosbigboy.tutandtam.entity.Contract;
import com.whosbigboy.tutandtam.entity.Guide;
import com.whosbigboy.tutandtam.entity.Manager;
import com.whosbigboy.tutandtam.entity.MemberList;
import com.whosbigboy.tutandtam.entity.Tourist;
import com.whosbigboy.tutandtam.repository.ContractRepository;
import com.whosbigboy.tutandtam.repository.GuideRepository;
import com.whosbigboy.tutandtam.repository.ManagerRepository;
import com.whosbigboy.tutandtam.repository.MemberListRepository;
import com.whosbigboy.tutandtam.repository.TourRepository;
import com.whosbigboy.tutandtam.repository.TouristRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final ContractRepository contractRepository;
    private final MemberListRepository memberListRepository;
    private final TouristRepository touristRepository;
    private final GuideRepository guideRepository;
    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TourRepository tourRepository;

    public AuthService(ContractRepository contractRepository,
                       MemberListRepository memberListRepository,
                       TouristRepository touristRepository,
                       GuideRepository guideRepository,
                       ManagerRepository managerRepository,
                       PasswordEncoder passwordEncoder,
                       TourRepository tourRepository) {
        this.contractRepository = contractRepository;
        this.memberListRepository = memberListRepository;
        this.touristRepository = touristRepository;
        this.guideRepository = guideRepository;
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
        this.tourRepository = tourRepository;
    }

    public AuthDtos.ContractAuthResponse checkContract(String contractNumber) {
        return contractRepository.findByNumHash(contractNumber)
                .map(this::buildContractResponse)
                .orElseGet(() -> new AuthDtos.ContractAuthResponse(false, Collections.emptyList()));
    }

    public AuthDtos.LoginResponse authenticateGuide(String email, String password) {
        return guideRepository.findByEmail(email)
                .filter(guide -> passwordEncoder.matches(password, guide.getPasswordHash()))
                .map(guide -> new AuthDtos.LoginResponse(true, guide.getId(), guide.getFio()))
                .orElseGet(() -> new AuthDtos.LoginResponse(false, null, null));
    }

    public AuthDtos.LoginResponse authenticateManager(String email, String password) {
        return managerRepository.findByEmail(email)
                .filter(manager -> passwordEncoder.matches(password, manager.getPasswordHash()))
                .map(manager -> new AuthDtos.LoginResponse(true, manager.getId(), manager.getFio()))
                .orElseGet(() -> new AuthDtos.LoginResponse(false, null, null));
    }

    public List<Map<String, Object>> listPublicTours() {
        return tourRepository.findByActiveTrue().stream()
                .map(tour -> Map.<String, Object>of(
                        "tourId", tour.getId(),
                        "name", tour.getName(),
                        "description", tour.getDescription()))
                .collect(Collectors.toList());
    }

    private AuthDtos.ContractAuthResponse buildContractResponse(Contract contract) {
        List<MemberList> members = memberListRepository.findByContractId(contract.getId());
        List<String> ids = members.stream().map(MemberList::getTouristId).distinct().limit(8).collect(Collectors.toList());
        Map<String, Tourist> touristsById = touristRepository.findByIdIn(ids).stream()
                .collect(Collectors.toMap(Tourist::getId, Function.identity()));
        List<AuthDtos.TouristIdentity> touristIdentities = ids.stream()
                .map(touristsById::get)
                .filter(t -> t != null)
                .map(t -> new AuthDtos.TouristIdentity(t.getId(), t.getFio()))
                .collect(Collectors.toList());
        return new AuthDtos.ContractAuthResponse(true, touristIdentities);
    }
}
