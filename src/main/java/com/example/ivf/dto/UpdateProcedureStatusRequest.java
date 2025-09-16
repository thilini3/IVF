package com.example.ivf.dto;

import com.example.ivf.domain.enums.ProcedureStatus;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record UpdateProcedureStatusRequest(
    @NotNull ProcedureStatus status,
    OffsetDateTime performedAt
) {
}
