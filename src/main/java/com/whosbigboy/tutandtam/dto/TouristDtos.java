package com.whosbigboy.tutandtam.dto;

import java.time.Instant;
import java.util.List;

public final class TouristDtos {
    private TouristDtos() {
    }

    public record GroupInfoResponse(
            String groupId,
            String groupName,
            Integer participants,
            List<AuthDtos.TouristIdentity> members
    ) {
    }

    public record TourInfoResponse(
            String tourId,
            String tourName,
            String description,
            Boolean active,
            List<TourEventStatus> events
    ) {
    }

    public record TourEventStatus(
            String eventName,
            Instant eventDate,
            Integer status
    ) {
    }

    public record GuideContactResponse(
            String guideId,
            String guideFio,
            String guidePhone,
            String guideSocialId
    ) {
    }
}
