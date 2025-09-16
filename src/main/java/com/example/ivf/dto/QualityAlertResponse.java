package com.example.ivf.dto;

import com.example.ivf.domain.enums.QualitySeverity;
import java.time.OffsetDateTime;
import java.util.UUID;

public record QualityAlertResponse(
    UUID id,
    String category,
    QualitySeverity severity,
    String description,
    OffsetDateTime detectedAt,
    boolean resolved,
    OffsetDateTime resolvedAt,
    String correctiveAction,
    UUID procedureId
) {
}
