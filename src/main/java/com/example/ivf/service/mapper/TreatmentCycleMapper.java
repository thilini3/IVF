package com.example.ivf.service.mapper;

import com.example.ivf.domain.CycleTimelineEvent;
import com.example.ivf.domain.EmbryoRecord;
import com.example.ivf.domain.LaboratoryProcedure;
import com.example.ivf.domain.QualityAlert;
import com.example.ivf.domain.TreatmentCycle;
import com.example.ivf.domain.value.ChecklistItem;
import com.example.ivf.domain.value.CryoStorageLocation;
import com.example.ivf.dto.ChecklistItemDto;
import com.example.ivf.dto.ChecklistItemResponse;
import com.example.ivf.dto.CryoStorageLocationResponse;
import com.example.ivf.dto.CycleTimelineEventResponse;
import com.example.ivf.dto.EmbryoRecordResponse;
import com.example.ivf.dto.LaboratoryProcedureResponse;
import com.example.ivf.dto.QualityAlertResponse;
import com.example.ivf.dto.TreatmentCycleResponse;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TreatmentCycleMapper {

    public TreatmentCycleResponse toResponse(TreatmentCycle cycle) {
        return new TreatmentCycleResponse(
            cycle.getId(),
            cycle.getCycleNumber(),
            cycle.getType(),
            cycle.getStatus(),
            cycle.getCoupleId(),
            cycle.getClinicianId(),
            cycle.getStartDate(),
            cycle.getEndDate(),
            cycle.getConsentCodes(),
            cycle.getTimelineEvents().stream().map(this::toResponse).toList(),
            cycle.getLaboratoryProcedures().stream().map(this::toResponse).toList(),
            cycle.getEmbryoRecords().stream().map(this::toResponse).toList(),
            cycle.getQualityAlerts().stream().map(this::toResponse).toList()
        );
    }

    public CycleTimelineEventResponse toResponse(CycleTimelineEvent event) {
        return new CycleTimelineEventResponse(
            event.getId(),
            event.getEventType(),
            event.getDescription(),
            event.getScheduledAt(),
            event.getCompletedAt(),
            event.getStatus(),
            event.getNotes()
        );
    }

    public LaboratoryProcedureResponse toResponse(LaboratoryProcedure procedure) {
        Set<ChecklistItemResponse> checklist = procedure.getQualityChecklist().stream()
            .map(this::toResponse)
            .collect(Collectors.toSet());
        return new LaboratoryProcedureResponse(
            procedure.getId(),
            procedure.getProcedureType(),
            procedure.getStatus(),
            procedure.getScheduledAt(),
            procedure.getPerformedAt(),
            procedure.getEmbryologistId(),
            procedure.getWitnessId(),
            procedure.getNotes(),
            checklist
        );
    }

    public ChecklistItemResponse toResponse(ChecklistItem item) {
        return new ChecklistItemResponse(item.getName(), item.getStatus(), item.getComment());
    }

    public ChecklistItem toChecklistItem(ChecklistItemDto dto) {
        if (dto == null) {
            return null;
        }
        return new ChecklistItem(dto.name(), dto.status(), dto.comment());
    }

    public EmbryoRecordResponse toResponse(EmbryoRecord record) {
        return new EmbryoRecordResponse(
            record.getId(),
            record.getEmbryoNumber(),
            record.getStage(),
            record.getGrade(),
            record.getStatus(),
            record.getObservationTime(),
            record.isCryopreserved(),
            record.getCryoDate(),
            toResponse(record.getCryoLocation())
        );
    }

    public CryoStorageLocationResponse toResponse(CryoStorageLocation location) {
        if (location == null) {
            return null;
        }
        return new CryoStorageLocationResponse(
            location.getFacility(),
            location.getTank(),
            location.getCanister(),
            location.getPosition()
        );
    }

    public CryoStorageLocation toCryoStorageLocation(String facility, String tank, String canister, String position) {
        if (facility == null) {
            return null;
        }
        return new CryoStorageLocation(facility, tank, canister, position);
    }

    public QualityAlertResponse toResponse(QualityAlert alert) {
        UUID procedureId = alert.getProcedure() != null ? alert.getProcedure().getId() : null;
        return new QualityAlertResponse(
            alert.getId(),
            alert.getCategory(),
            alert.getSeverity(),
            alert.getDescription(),
            alert.getDetectedAt(),
            alert.isResolved(),
            alert.getResolvedAt(),
            alert.getCorrectiveAction(),
            procedureId
        );
    }
}
