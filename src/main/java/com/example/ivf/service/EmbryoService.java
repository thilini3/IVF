package com.example.ivf.service;

import com.example.ivf.domain.EmbryoRecord;
import com.example.ivf.domain.TreatmentCycle;
import com.example.ivf.domain.enums.CycleStatus;
import com.example.ivf.domain.enums.EmbryoGrade;
import com.example.ivf.domain.enums.EmbryoStage;
import com.example.ivf.domain.enums.EmbryoStatus;
import com.example.ivf.domain.value.CryoStorageLocation;
import com.example.ivf.exception.BusinessRuleViolationException;
import com.example.ivf.exception.ResourceNotFoundException;
import com.example.ivf.repository.EmbryoRecordRepository;
import com.example.ivf.repository.TreatmentCycleRepository;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmbryoService {

    private final EmbryoRecordRepository embryoRecordRepository;
    private final TreatmentCycleRepository treatmentCycleRepository;

    public EmbryoService(EmbryoRecordRepository embryoRecordRepository, TreatmentCycleRepository treatmentCycleRepository) {
        this.embryoRecordRepository = embryoRecordRepository;
        this.treatmentCycleRepository = treatmentCycleRepository;
    }

    public EmbryoRecord recordObservation(UUID cycleId, int embryoNumber, EmbryoStage stage, EmbryoGrade grade,
                                          EmbryoStatus status, OffsetDateTime observationTime) {
        TreatmentCycle cycle = treatmentCycleRepository.findById(cycleId)
            .orElseThrow(() -> new ResourceNotFoundException("Treatment cycle not found: " + cycleId));
        if (cycle.getStatus() == CycleStatus.CANCELLED) {
            throw new BusinessRuleViolationException("Cannot record embryo observations for cancelled cycles");
        }
        EmbryoRecord record = new EmbryoRecord(embryoNumber, stage, grade, status, observationTime);
        cycle.addEmbryoRecord(record);
        return embryoRecordRepository.save(record);
    }

    public EmbryoRecord updateEmbryo(UUID embryoId, EmbryoStage stage, EmbryoGrade grade,
                                     EmbryoStatus status, OffsetDateTime observationTime) {
        EmbryoRecord record = embryoRecordRepository.findById(embryoId)
            .orElseThrow(() -> new ResourceNotFoundException("Embryo record not found: " + embryoId));
        if (stage != null) {
            record.setStage(stage);
        }
        if (grade != null) {
            record.setGrade(grade);
        }
        if (status != null) {
            record.setStatus(status);
        }
        if (observationTime != null) {
            record.setObservationTime(observationTime);
        }
        return record;
    }

    public EmbryoRecord markCryopreserved(UUID embryoId, CryoStorageLocation location, OffsetDateTime cryoDate) {
        EmbryoRecord record = embryoRecordRepository.findById(embryoId)
            .orElseThrow(() -> new ResourceNotFoundException("Embryo record not found: " + embryoId));
        record.markCryopreserved(location, cryoDate != null ? cryoDate : OffsetDateTime.now());
        return record;
    }

    @Transactional
    public List<EmbryoRecord> getByCycle(UUID cycleId) {
        return embryoRecordRepository.findByCycle_Id(cycleId);
    }
}
