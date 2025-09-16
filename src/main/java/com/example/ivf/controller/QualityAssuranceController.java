package com.example.ivf.controller;

import com.example.ivf.domain.QualityAlert;
import com.example.ivf.dto.QualityAlertRequest;
import com.example.ivf.dto.QualityAlertResponse;
import com.example.ivf.dto.ResolveQualityAlertRequest;
import com.example.ivf.service.QualityAssuranceService;
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
@RequestMapping("/api/cycles/{cycleId}/quality-alerts")
@Validated
public class QualityAssuranceController {

    private final QualityAssuranceService qualityAssuranceService;
    private final TreatmentCycleMapper mapper;

    public QualityAssuranceController(QualityAssuranceService qualityAssuranceService, TreatmentCycleMapper mapper) {
        this.qualityAssuranceService = qualityAssuranceService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<QualityAlertResponse> createAlert(@PathVariable UUID cycleId,
                                                            @Valid @RequestBody QualityAlertRequest request) {
        QualityAlert alert = qualityAssuranceService.createAlert(
            cycleId,
            request.procedureId(),
            request.category(),
            request.severity(),
            request.description(),
            request.detectedAt()
        );
        return ResponseEntity
            .created(URI.create("/api/cycles/" + cycleId + "/quality-alerts/" + alert.getId()))
            .body(mapper.toResponse(alert));
    }

    @GetMapping
    public List<QualityAlertResponse> listAlerts(@PathVariable UUID cycleId) {
        return qualityAssuranceService.getAlertsForCycle(cycleId).stream()
            .map(mapper::toResponse)
            .toList();
    }

    @PatchMapping("/{alertId}")
    public QualityAlertResponse resolveAlert(@PathVariable UUID alertId,
                                              @Valid @RequestBody ResolveQualityAlertRequest request) {
        QualityAlert alert = qualityAssuranceService.resolveAlert(alertId, request.correctiveAction(), request.resolvedAt());
        return mapper.toResponse(alert);
    }
}
