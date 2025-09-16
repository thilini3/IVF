package com.example.ivf.dto;

import com.example.ivf.domain.enums.CycleStatus;
import com.example.ivf.domain.enums.CycleType;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record TreatmentCycleResponse(
    UUID id,
    String cycleNumber,
    CycleType type,
    CycleStatus status,
    String coupleId,
    String clinicianId,
    LocalDate startDate,
    LocalDate endDate,
    Set<String> consentCodes,
    List<CycleTimelineEventResponse> timelineEvents,
    List<LaboratoryProcedureResponse> laboratoryProcedures,
    List<EmbryoRecordResponse> embryoRecords,
    List<QualityAlertResponse> qualityAlerts
) {
}
