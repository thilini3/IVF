package com.example.ivf.service;

import com.example.ivf.domain.LaboratoryProcedure;
import com.example.ivf.domain.TreatmentCycle;
import com.example.ivf.domain.enums.CycleStatus;
import com.example.ivf.domain.enums.ProcedureStatus;
import com.example.ivf.domain.enums.ProcedureType;
import com.example.ivf.domain.value.ChecklistItem;
import com.example.ivf.exception.BusinessRuleViolationException;
import com.example.ivf.exception.ResourceNotFoundException;
import com.example.ivf.repository.LaboratoryProcedureRepository;
import com.example.ivf.repository.TreatmentCycleRepository;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LaboratoryProcedureService {

    private final LaboratoryProcedureRepository laboratoryProcedureRepository;
    private final TreatmentCycleRepository treatmentCycleRepository;

    public LaboratoryProcedureService(LaboratoryProcedureRepository laboratoryProcedureRepository,
                                      TreatmentCycleRepository treatmentCycleRepository) {
        this.laboratoryProcedureRepository = laboratoryProcedureRepository;
        this.treatmentCycleRepository = treatmentCycleRepository;
    }

    public LaboratoryProcedure scheduleProcedure(UUID cycleId, ProcedureType type, OffsetDateTime scheduledAt,
                                                 String embryologistId, String witnessId, String notes, List<ChecklistItem> checklistItems) {
        TreatmentCycle cycle = treatmentCycleRepository.findById(cycleId)
            .orElseThrow(() -> new ResourceNotFoundException("Treatment cycle not found: " + cycleId));
        if (cycle.getStatus() == CycleStatus.CANCELLED) {
            throw new BusinessRuleViolationException("Cannot schedule procedures for cancelled cycles");
        }
        LaboratoryProcedure procedure = new LaboratoryProcedure(type, ProcedureStatus.PLANNED, scheduledAt, embryologistId, witnessId, notes);
        if (checklistItems != null) {
            checklistItems.forEach(procedure::addChecklistItem);
        }
        cycle.addLaboratoryProcedure(procedure);
        return laboratoryProcedureRepository.save(procedure);
    }

    @Transactional
    public List<LaboratoryProcedure> getProceduresForCycle(UUID cycleId) {
        TreatmentCycle cycle = treatmentCycleRepository.findById(cycleId)
            .orElseThrow(() -> new ResourceNotFoundException("Treatment cycle not found: " + cycleId));
        return cycle.getLaboratoryProcedures();
    }

    public LaboratoryProcedure updateStatus(UUID cycleId, UUID procedureId, ProcedureStatus status, OffsetDateTime performedAt) {
        LaboratoryProcedure procedure = laboratoryProcedureRepository.findById(procedureId)
            .orElseThrow(() -> new ResourceNotFoundException("Laboratory procedure not found: " + procedureId));
        ensureProcedureBelongsToCycle(cycleId, procedure);
        procedure.setStatus(status);
        if (status == ProcedureStatus.COMPLETED) {
            procedure.setPerformedAt(performedAt != null ? performedAt : OffsetDateTime.now());
        }
        return procedure;
    }

    public LaboratoryProcedure addChecklistItem(UUID cycleId, UUID procedureId, ChecklistItem item) {
        LaboratoryProcedure procedure = laboratoryProcedureRepository.findById(procedureId)
            .orElseThrow(() -> new ResourceNotFoundException("Laboratory procedure not found: " + procedureId));
        ensureProcedureBelongsToCycle(cycleId, procedure);
        procedure.addChecklistItem(item);
        return procedure;
    }

    private void ensureProcedureBelongsToCycle(UUID cycleId, LaboratoryProcedure procedure) {
        if (cycleId == null) {
            return;
        }
        TreatmentCycle cycle = procedure.getCycle();
        if (cycle != null && !cycle.getId().equals(cycleId)) {
            throw new BusinessRuleViolationException("Procedure does not belong to the specified cycle");
        }
    }
}
