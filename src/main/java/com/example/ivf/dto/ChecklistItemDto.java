package com.example.ivf.dto;

import com.example.ivf.domain.enums.ChecklistStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChecklistItemDto(
    @NotBlank String name,
    @NotNull ChecklistStatus status,
    String comment
) {
}
