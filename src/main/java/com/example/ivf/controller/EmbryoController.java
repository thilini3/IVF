package com.example.ivf.controller;

import com.example.ivf.domain.EmbryoRecord;
import com.example.ivf.domain.value.CryoStorageLocation;
import com.example.ivf.dto.CryopreservationRequest;
import com.example.ivf.dto.EmbryoObservationRequest;
import com.example.ivf.dto.EmbryoRecordResponse;
import com.example.ivf.dto.UpdateEmbryoRequest;
import com.example.ivf.service.EmbryoService;
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
@RequestMapping("/api/cycles/{cycleId}/embryos")
@Validated
public class EmbryoController {

    private final EmbryoService embryoService;
    private final TreatmentCycleMapper mapper;

    public EmbryoController(EmbryoService embryoService, TreatmentCycleMapper mapper) {
        this.embryoService = embryoService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<EmbryoRecordResponse> recordObservation(@PathVariable UUID cycleId,
                                                                   @Valid @RequestBody EmbryoObservationRequest request) {
        EmbryoRecord record = embryoService.recordObservation(
            cycleId,
            request.embryoNumber(),
            request.stage(),
            request.grade(),
            request.status(),
            request.observationTime()
        );
        return ResponseEntity
            .created(URI.create("/api/cycles/" + cycleId + "/embryos/" + record.getId()))
            .body(mapper.toResponse(record));
    }

    @GetMapping
    public List<EmbryoRecordResponse> listEmbryos(@PathVariable UUID cycleId) {
        return embryoService.getByCycle(cycleId).stream()
            .map(mapper::toResponse)
            .toList();
    }

    @PatchMapping("/{embryoId}")
    public EmbryoRecordResponse updateEmbryo(@PathVariable UUID embryoId,
                                             @Valid @RequestBody UpdateEmbryoRequest request) {
        EmbryoRecord record = embryoService.updateEmbryo(embryoId, request.stage(), request.grade(), request.status(), request.observationTime());
        return mapper.toResponse(record);
    }

    @PostMapping("/{embryoId}/cryopreservation")
    public EmbryoRecordResponse markCryopreserved(@PathVariable UUID embryoId,
                                                  @Valid @RequestBody CryopreservationRequest request) {
        CryoStorageLocation location = mapper.toCryoStorageLocation(request.facility(), request.tank(), request.canister(), request.position());
        EmbryoRecord record = embryoService.markCryopreserved(embryoId, location, request.cryoDate());
        return mapper.toResponse(record);
    }
}
