package com.example.ivf.dto;

import com.example.ivf.domain.enums.ProcedureType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;

public record ScheduleProcedureRequest(
    @NotNull ProcedureType procedureType,
    @NotNull OffsetDateTime scheduledAt,
    String embryologistId,
    String witnessId,
    String notes,
    @Valid List<ChecklistItemDto> checklist
) {
}
