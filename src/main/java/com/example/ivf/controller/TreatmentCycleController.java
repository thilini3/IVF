package com.example.ivf.controller;

import com.example.ivf.domain.CycleTimelineEvent;
import com.example.ivf.domain.TreatmentCycle;
import com.example.ivf.dto.CreateTreatmentCycleRequest;
import com.example.ivf.dto.TimelineEventRequest;
import com.example.ivf.dto.TreatmentCycleResponse;
import com.example.ivf.dto.UpdateCycleStatusRequest;
import com.example.ivf.service.TreatmentCycleService;
import com.example.ivf.service.mapper.TreatmentCycleMapper;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Set;
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
@RequestMapping("/api/cycles")
@Validated
public class TreatmentCycleController {

    private final TreatmentCycleService treatmentCycleService;
    private final TreatmentCycleMapper mapper;

    public TreatmentCycleController(TreatmentCycleService treatmentCycleService, TreatmentCycleMapper mapper) {
        this.treatmentCycleService = treatmentCycleService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<TreatmentCycleResponse> createCycle(@Valid @RequestBody CreateTreatmentCycleRequest request) {
        Set<String> consentCodes = request.consentCodes() != null ? request.consentCodes() : Set.of();
        TreatmentCycle cycle = treatmentCycleService.createCycle(request.type(), request.coupleId(), request.clinicianId(), request.startDate(), consentCodes);
        return ResponseEntity
            .created(URI.create("/api/cycles/" + cycle.getId()))
            .body(mapper.toResponse(cycle));
    }

    @GetMapping
    public List<TreatmentCycleResponse> listCycles() {
        return treatmentCycleService.findAll().stream()
            .map(mapper::toResponse)
            .toList();
    }

    @GetMapping("/{cycleId}")
    public TreatmentCycleResponse getCycle(@PathVariable UUID cycleId) {
        TreatmentCycle cycle = treatmentCycleService.getCycle(cycleId);
        return mapper.toResponse(cycle);
    }

    @PatchMapping("/{cycleId}/status")
    public TreatmentCycleResponse updateStatus(@PathVariable UUID cycleId, @Valid @RequestBody UpdateCycleStatusRequest request) {
        TreatmentCycle cycle = treatmentCycleService.updateStatus(cycleId, request.status(), request.endDate());
        return mapper.toResponse(cycle);
    }

    @PostMapping("/{cycleId}/timeline-events")
    public ResponseEntity<TreatmentCycleResponse> addTimelineEvent(@PathVariable UUID cycleId,
                                                                   @Valid @RequestBody TimelineEventRequest request) {
        CycleTimelineEvent event = treatmentCycleService.addTimelineEvent(cycleId, request.eventType(), request.description(), request.scheduledAt(), request.status(), request.notes());
        TreatmentCycle cycle = treatmentCycleService.getCycle(cycleId);
        return ResponseEntity
            .created(URI.create("/api/cycles/" + cycleId + "/timeline-events/" + event.getId()))
            .body(mapper.toResponse(cycle));
    }

    @PostMapping("/{cycleId}/consents/{consentCode}")
    public ResponseEntity<Void> addConsent(@PathVariable UUID cycleId, @PathVariable String consentCode) {
        treatmentCycleService.recordConsent(cycleId, consentCode);
        return ResponseEntity.noContent().build();
    }
}
