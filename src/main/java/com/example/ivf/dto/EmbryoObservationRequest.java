package com.example.ivf.dto;

import com.example.ivf.domain.enums.EmbryoGrade;
import com.example.ivf.domain.enums.EmbryoStage;
import com.example.ivf.domain.enums.EmbryoStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record EmbryoObservationRequest(
    @Min(1) int embryoNumber,
    @NotNull EmbryoStage stage,
    @NotNull EmbryoGrade grade,
    @NotNull EmbryoStatus status,
    @NotNull OffsetDateTime observationTime
) {
}
