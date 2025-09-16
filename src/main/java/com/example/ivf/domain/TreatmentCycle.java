package com.example.ivf.domain;

import com.example.ivf.domain.enums.CycleStatus;
import com.example.ivf.domain.enums.CycleType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "treatment_cycles")
public class TreatmentCycle {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "cycle_number", nullable = false, unique = true)
    private String cycleNumber;

    @Enumerated(EnumType.STRING)
    private CycleType type;

    @Enumerated(EnumType.STRING)
    private CycleStatus status;

    @NotBlank
    @Column(name = "couple_id", nullable = false)
    private String coupleId;

    private String clinicianId;

    private LocalDate startDate;

    private LocalDate endDate;

    @ElementCollection
    @CollectionTable(name = "cycle_consents", joinColumns = @JoinColumn(name = "cycle_id"))
    @Column(name = "consent_code")
    private Set<String> consentCodes = new HashSet<>();

    @OneToMany(mappedBy = "cycle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CycleTimelineEvent> timelineEvents = new ArrayList<>();

    @OneToMany(mappedBy = "cycle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LaboratoryProcedure> laboratoryProcedures = new ArrayList<>();

    @OneToMany(mappedBy = "cycle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmbryoRecord> embryoRecords = new ArrayList<>();

    @OneToMany(mappedBy = "cycle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QualityAlert> qualityAlerts = new ArrayList<>();

    protected TreatmentCycle() {
        // for JPA
    }

    public TreatmentCycle(String cycleNumber, @NotNull CycleType type, @NotBlank String coupleId, String clinicianId, LocalDate startDate) {
        this.cycleNumber = cycleNumber;
        this.type = type;
        this.status = CycleStatus.PLANNED;
        this.coupleId = coupleId;
        this.clinicianId = clinicianId;
        this.startDate = startDate;
    }

    public UUID getId() {
        return id;
    }

    public String getCycleNumber() {
        return cycleNumber;
    }

    public CycleType getType() {
        return type;
    }

    public void setType(CycleType type) {
        this.type = type;
    }

    public CycleStatus getStatus() {
        return status;
    }

    public void setStatus(CycleStatus status) {
        this.status = status;
    }

    public String getCoupleId() {
        return coupleId;
    }

    public String getClinicianId() {
        return clinicianId;
    }

    public void setClinicianId(String clinicianId) {
        this.clinicianId = clinicianId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<String> getConsentCodes() {
        return consentCodes;
    }

    public List<CycleTimelineEvent> getTimelineEvents() {
        return timelineEvents;
    }

    public List<LaboratoryProcedure> getLaboratoryProcedures() {
        return laboratoryProcedures;
    }

    public List<EmbryoRecord> getEmbryoRecords() {
        return embryoRecords;
    }

    public List<QualityAlert> getQualityAlerts() {
        return qualityAlerts;
    }

    public void addConsent(String consentCode) {
        this.consentCodes.add(consentCode);
    }

    public void addTimelineEvent(CycleTimelineEvent event) {
        timelineEvents.add(event);
        event.setCycle(this);
    }

    public void addLaboratoryProcedure(LaboratoryProcedure procedure) {
        laboratoryProcedures.add(procedure);
        procedure.setCycle(this);
    }

    public void addEmbryoRecord(EmbryoRecord embryoRecord) {
        embryoRecords.add(embryoRecord);
        embryoRecord.setCycle(this);
    }

    public void addQualityAlert(QualityAlert alert) {
        qualityAlerts.add(alert);
        alert.setCycle(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreatmentCycle that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
