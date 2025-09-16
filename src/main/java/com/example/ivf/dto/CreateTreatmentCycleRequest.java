package com.example.ivf.dto;

import com.example.ivf.domain.enums.CycleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public record CreateTreatmentCycleRequest(
    @NotNull CycleType type,
    @NotBlank String coupleId,
    String clinicianId,
    @NotNull LocalDate startDate,
    Set<String> consentCodes
) {
}
