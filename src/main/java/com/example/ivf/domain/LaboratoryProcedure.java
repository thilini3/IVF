package com.example.ivf.domain;

import com.example.ivf.domain.enums.ProcedureStatus;
import com.example.ivf.domain.enums.ProcedureType;
import com.example.ivf.domain.value.ChecklistItem;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "laboratory_procedures")
public class LaboratoryProcedure {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id")
    private TreatmentCycle cycle;

    @Enumerated(EnumType.STRING)
    private ProcedureType procedureType;

    @Enumerated(EnumType.STRING)
    private ProcedureStatus status;

    private OffsetDateTime scheduledAt;

    private OffsetDateTime performedAt;

    private String embryologistId;

    private String witnessId;

    private String notes;

    @ElementCollection
    @CollectionTable(name = "procedure_checklist", joinColumns = @JoinColumn(name = "procedure_id"))
    private Set<ChecklistItem> qualityChecklist = new HashSet<>();

    protected LaboratoryProcedure() {
        // for JPA
    }

    public LaboratoryProcedure(ProcedureType procedureType, ProcedureStatus status, OffsetDateTime scheduledAt, String embryologistId, String witnessId, String notes) {
        this.procedureType = procedureType;
        this.status = status;
        this.scheduledAt = scheduledAt;
        this.embryologistId = embryologistId;
        this.witnessId = witnessId;
        this.notes = notes;
    }

    public UUID getId() {
        return id;
    }

    public TreatmentCycle getCycle() {
        return cycle;
    }

    public void setCycle(TreatmentCycle cycle) {
        this.cycle = cycle;
    }

    public ProcedureType getProcedureType() {
        return procedureType;
    }

    public ProcedureStatus getStatus() {
        return status;
    }

    public void setStatus(ProcedureStatus status) {
        this.status = status;
    }

    public OffsetDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(OffsetDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public OffsetDateTime getPerformedAt() {
        return performedAt;
    }

    public void setPerformedAt(OffsetDateTime performedAt) {
        this.performedAt = performedAt;
    }

    public String getEmbryologistId() {
        return embryologistId;
    }

    public void setEmbryologistId(String embryologistId) {
        this.embryologistId = embryologistId;
    }

    public String getWitnessId() {
        return witnessId;
    }

    public void setWitnessId(String witnessId) {
        this.witnessId = witnessId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<ChecklistItem> getQualityChecklist() {
        return qualityChecklist;
    }

    public void addChecklistItem(ChecklistItem item) {
        qualityChecklist.add(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LaboratoryProcedure that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
