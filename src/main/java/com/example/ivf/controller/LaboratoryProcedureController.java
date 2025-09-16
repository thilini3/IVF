package com.example.ivf.controller;

import com.example.ivf.domain.LaboratoryProcedure;
import com.example.ivf.domain.value.ChecklistItem;
import com.example.ivf.dto.ChecklistItemDto;
import com.example.ivf.dto.LaboratoryProcedureResponse;
import com.example.ivf.dto.ScheduleProcedureRequest;
import com.example.ivf.dto.UpdateProcedureStatusRequest;
import com.example.ivf.service.LaboratoryProcedureService;
import com.example.ivf.service.mapper.TreatmentCycleMapper;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cycles/{cycleId}/procedures")
@Validated
public class LaboratoryProcedureController {

    private final LaboratoryProcedureService laboratoryProcedureService;
    private final TreatmentCycleMapper mapper;

    public LaboratoryProcedureController(LaboratoryProcedureService laboratoryProcedureService, TreatmentCycleMapper mapper) {
        this.laboratoryProcedureService = laboratoryProcedureService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<LaboratoryProcedureResponse> scheduleProcedure(@PathVariable UUID cycleId,
                                                                          @Valid @RequestBody ScheduleProcedureRequest request) {
        List<ChecklistItem> checklist = request.checklist() != null
            ? request.checklist().stream().map(mapper::toChecklistItem).toList()
            : List.of();
        LaboratoryProcedure procedure = laboratoryProcedureService.scheduleProcedure(
            cycleId,
            request.procedureType(),
            request.scheduledAt(),
            request.embryologistId(),
            request.witnessId(),
            request.notes(),
            checklist
        );
        return ResponseEntity
            .created(URI.create("/api/cycles/" + cycleId + "/procedures/" + procedure.getId()))
            .body(mapper.toResponse(procedure));
    }

    @GetMapping
    public List<LaboratoryProcedureResponse> listProcedures(@PathVariable UUID cycleId) {
        return laboratoryProcedureService.getProceduresForCycle(cycleId).stream()
            .map(mapper::toResponse)
            .toList();
    }

    @PatchMapping("/{procedureId}/status")
    public LaboratoryProcedureResponse updateStatus(@PathVariable UUID cycleId,
                                                     @PathVariable UUID procedureId,
                                                     @Valid @RequestBody UpdateProcedureStatusRequest request) {
        LaboratoryProcedure updated = laboratoryProcedureService.updateStatus(cycleId, procedureId, request.status(), request.performedAt());
        return mapper.toResponse(updated);
    }

    @PostMapping("/{procedureId}/checklist")
    public LaboratoryProcedureResponse addChecklistItem(@PathVariable UUID cycleId,
                                                         @PathVariable UUID procedureId,
                                                         @Valid @RequestBody ChecklistItemDto request) {
        ChecklistItem item = mapper.toChecklistItem(request);
        LaboratoryProcedure updated = laboratoryProcedureService.addChecklistItem(cycleId, procedureId, item);
        return mapper.toResponse(updated);
    }
}
