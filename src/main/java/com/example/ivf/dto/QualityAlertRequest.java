package com.example.ivf.dto;

import com.example.ivf.domain.enums.QualitySeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

public record QualityAlertRequest(
    UUID procedureId,
    @NotBlank String category,
    @NotNull QualitySeverity severity,
    @NotBlank String description,
    OffsetDateTime detectedAt
) {
}
