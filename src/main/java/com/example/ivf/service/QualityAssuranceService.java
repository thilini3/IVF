package com.example.ivf.service;

import com.example.ivf.domain.LaboratoryProcedure;
import com.example.ivf.domain.QualityAlert;
import com.example.ivf.domain.TreatmentCycle;
import com.example.ivf.domain.enums.QualitySeverity;
import com.example.ivf.exception.ResourceNotFoundException;
import com.example.ivf.repository.LaboratoryProcedureRepository;
import com.example.ivf.repository.QualityAlertRepository;
import com.example.ivf.repository.TreatmentCycleRepository;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class QualityAssuranceService {

    private final QualityAlertRepository qualityAlertRepository;
    private final TreatmentCycleRepository treatmentCycleRepository;
    private final LaboratoryProcedureRepository laboratoryProcedureRepository;

    public QualityAssuranceService(QualityAlertRepository qualityAlertRepository,
                                   TreatmentCycleRepository treatmentCycleRepository,
                                   LaboratoryProcedureRepository laboratoryProcedureRepository) {
        this.qualityAlertRepository = qualityAlertRepository;
        this.treatmentCycleRepository = treatmentCycleRepository;
        this.laboratoryProcedureRepository = laboratoryProcedureRepository;
    }

    public QualityAlert createAlert(UUID cycleId, UUID procedureId, String category, QualitySeverity severity,
                                    String description, OffsetDateTime detectedAt) {
        TreatmentCycle cycle = treatmentCycleRepository.findById(cycleId)
            .orElseThrow(() -> new ResourceNotFoundException("Treatment cycle not found: " + cycleId));
        LaboratoryProcedure procedure = null;
        if (procedureId != null) {
            procedure = laboratoryProcedureRepository.findById(procedureId)
                .orElseThrow(() -> new ResourceNotFoundException("Laboratory procedure not found: " + procedureId));
        }
        QualityAlert alert = new QualityAlert(category, severity, description, detectedAt != null ? detectedAt : OffsetDateTime.now());
        cycle.addQualityAlert(alert);
        if (procedure != null) {
            alert.setProcedure(procedure);
        }
        return qualityAlertRepository.save(alert);
    }

    public QualityAlert resolveAlert(UUID alertId, String correctiveAction, OffsetDateTime resolvedAt) {
        QualityAlert alert = qualityAlertRepository.findById(alertId)
            .orElseThrow(() -> new ResourceNotFoundException("Quality alert not found: " + alertId));
        alert.markResolved(resolvedAt != null ? resolvedAt : OffsetDateTime.now(), correctiveAction);
        return alert;
    }

    @Transactional
    public List<QualityAlert> getAlertsForCycle(UUID cycleId) {
        TreatmentCycle cycle = treatmentCycleRepository.findById(cycleId)
            .orElseThrow(() -> new ResourceNotFoundException("Treatment cycle not found: " + cycleId));
        return cycle.getQualityAlerts();
    }
}
