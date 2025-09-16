package com.example.ivf.service;

import com.example.ivf.domain.CycleTimelineEvent;
import com.example.ivf.domain.TreatmentCycle;
import com.example.ivf.domain.enums.CycleStatus;
import com.example.ivf.domain.enums.CycleType;
import com.example.ivf.domain.enums.TimelineEventType;
import com.example.ivf.domain.enums.TimelineStatus;
import com.example.ivf.exception.BusinessRuleViolationException;
import com.example.ivf.exception.ResourceNotFoundException;
import com.example.ivf.repository.TreatmentCycleRepository;
import com.example.ivf.service.integration.CycleIntegrationPublisher;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TreatmentCycleService {

    private final TreatmentCycleRepository treatmentCycleRepository;
    private final CycleNumberGenerator cycleNumberGenerator;
    private final CycleIntegrationPublisher integrationPublisher;

    public TreatmentCycleService(TreatmentCycleRepository treatmentCycleRepository,
                                 CycleNumberGenerator cycleNumberGenerator,
                                 CycleIntegrationPublisher integrationPublisher) {
        this.treatmentCycleRepository = treatmentCycleRepository;
        this.cycleNumberGenerator = cycleNumberGenerator;
        this.integrationPublisher = integrationPublisher;
    }

    public TreatmentCycle createCycle(CycleType type, String coupleId, String clinicianId, LocalDate startDate, Set<String> consentCodes) {
        String cycleNumber = cycleNumberGenerator.nextCycleNumber();
        TreatmentCycle cycle = new TreatmentCycle(cycleNumber, type, coupleId, clinicianId, startDate);
        consentCodes.forEach(cycle::addConsent);
        cycle.setStatus(CycleStatus.ACTIVE);
        TreatmentCycle saved = treatmentCycleRepository.save(cycle);
        integrationPublisher.publishCycleSnapshot(saved);
        return saved;
    }

    public TreatmentCycle getCycle(UUID cycleId) {
        return treatmentCycleRepository.findById(cycleId)
            .orElseThrow(() -> new ResourceNotFoundException("Treatment cycle not found: " + cycleId));
    }

    public List<TreatmentCycle> findAll() {
        return treatmentCycleRepository.findAll();
    }

    public TreatmentCycle updateStatus(UUID cycleId, CycleStatus newStatus, LocalDate endDate) {
        TreatmentCycle cycle = getCycle(cycleId);
        if (cycle.getStatus() == CycleStatus.CANCELLED || cycle.getStatus() == CycleStatus.COMPLETED) {
            throw new BusinessRuleViolationException("Cycle is already finalized and cannot change status.");
        }
        cycle.setStatus(newStatus);
        if (newStatus == CycleStatus.COMPLETED) {
            cycle.setEndDate(endDate != null ? endDate : LocalDate.now());
        } else if (newStatus == CycleStatus.CANCELLED) {
            cycle.setEndDate(endDate != null ? endDate : LocalDate.now());
        }
        integrationPublisher.notifyStatusChange(cycle);
        return cycle;
    }

    public CycleTimelineEvent addTimelineEvent(UUID cycleId, TimelineEventType eventType, String description, OffsetDateTime scheduledAt, TimelineStatus status, String notes) {
        TreatmentCycle cycle = getCycle(cycleId);
        if (cycle.getStatus() == CycleStatus.CANCELLED) {
            throw new BusinessRuleViolationException("Cannot add events to cancelled cycles");
        }
        CycleTimelineEvent event = new CycleTimelineEvent(eventType, description, scheduledAt, status, notes);
        cycle.addTimelineEvent(event);
        return event;
    }

    public void recordConsent(UUID cycleId, String consentCode) {
        TreatmentCycle cycle = getCycle(cycleId);
        cycle.addConsent(consentCode);
    }
}
