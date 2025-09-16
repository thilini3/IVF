package com.example.ivf.dto;

import com.example.ivf.domain.enums.CycleStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UpdateCycleStatusRequest(
    @NotNull CycleStatus status,
    LocalDate endDate
) {
}
