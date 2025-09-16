package com.example.ivf.dto;

import com.example.ivf.domain.enums.TimelineEventType;
import com.example.ivf.domain.enums.TimelineStatus;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CycleTimelineEventResponse(
    UUID id,
    TimelineEventType eventType,
    String description,
    OffsetDateTime scheduledAt,
    OffsetDateTime completedAt,
    TimelineStatus status,
    String notes
) {
}
