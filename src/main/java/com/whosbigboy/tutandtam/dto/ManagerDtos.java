package com.whosbigboy.tutandtam.dto;

import java.time.Instant;
import java.util.List;

public final class ManagerDtos {
    private ManagerDtos() {
    }

    public record CurrentGroupResponse(
            String groupId,
            String groupName,
            String tourId,
            String tourName,
            Integer participants
    ) {
    }

    public record TimelinePoint(
            String eventId,
            String eventName,
            Instant eventDate,
            String colorStatus
    ) {
    }

    public record TimelineResponse(
            String tourId,
            String tourName,
            List<TimelinePoint> points
    ) {
    }
}
