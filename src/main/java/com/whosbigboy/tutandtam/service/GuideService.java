package com.whosbigboy.tutandtam.service;

import com.whosbigboy.tutandtam.dto.AuthDtos;
import com.whosbigboy.tutandtam.dto.TouristDtos;
import com.whosbigboy.tutandtam.entity.GroupEntity;
import com.whosbigboy.tutandtam.entity.Guide;
import com.whosbigboy.tutandtam.entity.TouristsGroup;
import com.whosbigboy.tutandtam.exception.NotFoundException;
import com.whosbigboy.tutandtam.repository.GroupRepository;
import com.whosbigboy.tutandtam.repository.GuideRepository;
import com.whosbigboy.tutandtam.repository.TouristRepository;
import com.whosbigboy.tutandtam.repository.TouristsGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuideService {

    private static final Logger log = LoggerFactory.getLogger(GuideService.class);

    private final GuideRepository guideRepository;
    private final GroupRepository groupRepository;
    private final TouristsGroupRepository touristsGroupRepository;
    private final TouristRepository touristRepository;

    public GuideService(GuideRepository guideRepository,
                        GroupRepository groupRepository,
                        TouristsGroupRepository touristsGroupRepository,
                        TouristRepository touristRepository) {
        this.guideRepository = guideRepository;
        this.groupRepository = groupRepository;
        this.touristsGroupRepository = touristsGroupRepository;
        this.touristRepository = touristRepository;
    }

    public TouristDtos.GroupInfoResponse getOwnGroup(String guideSocialId) {
        Guide guide = guideRepository.findBySocialId(guideSocialId)
                .orElseThrow(() -> new NotFoundException("Гид не найден"));
        GroupEntity group = groupRepository.findByGuideId(guide.getId())
                .orElseThrow(() -> new NotFoundException("Группа гида не найдена"));

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

    public int broadcastMessage(String guideSocialId, String message) {
        Guide guide = guideRepository.findBySocialId(guideSocialId)
                .orElseThrow(() -> new NotFoundException("Гид не найден"));
        GroupEntity group = groupRepository.findByGuideId(guide.getId())
                .orElseThrow(() -> new NotFoundException("Группа гида не найдена"));
        List<TouristsGroup> recipients = touristsGroupRepository.findByGroupId(group.getId());
        recipients.forEach(recipient -> log.info("Guide broadcast from {} to tourist {}: {}", guide.getId(), recipient.getTouristId(), message));
        return recipients.size();
    }
}
