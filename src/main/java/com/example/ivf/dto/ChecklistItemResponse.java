package com.example.ivf.dto;

import com.example.ivf.domain.enums.ChecklistStatus;

public record ChecklistItemResponse(
    String name,
    ChecklistStatus status,
    String comment
) {
}
