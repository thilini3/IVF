package com.example.ivf.dto;

import com.example.ivf.domain.enums.ProcedureStatus;
import com.example.ivf.domain.enums.ProcedureType;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

public record LaboratoryProcedureResponse(
    UUID id,
    ProcedureType procedureType,
    ProcedureStatus status,
    OffsetDateTime scheduledAt,
    OffsetDateTime performedAt,
    String embryologistId,
    String witnessId,
    String notes,
    Set<ChecklistItemResponse> qualityChecklist
) {
}
