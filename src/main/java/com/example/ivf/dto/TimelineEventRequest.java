package com.example.ivf.dto;

import com.example.ivf.domain.enums.TimelineEventType;
import com.example.ivf.domain.enums.TimelineStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record TimelineEventRequest(
    @NotNull TimelineEventType eventType,
    @NotBlank String description,
    @NotNull OffsetDateTime scheduledAt,
    @NotNull TimelineStatus status,
    String notes
) {
}
