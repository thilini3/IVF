package com.example.ivf.domain;

import com.example.ivf.domain.enums.QualitySeverity;
import jakarta.persistence.Column;
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
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "quality_alerts")
public class QualityAlert {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id")
    private TreatmentCycle cycle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_id")
    private LaboratoryProcedure procedure;

    @Enumerated(EnumType.STRING)
    private QualitySeverity severity;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, length = 2000)
    private String description;

    private OffsetDateTime detectedAt;

    private boolean resolved;

    private OffsetDateTime resolvedAt;

    private String correctiveAction;

    protected QualityAlert() {
        // for JPA
    }

    public QualityAlert(String category, QualitySeverity severity, String description, OffsetDateTime detectedAt) {
        this.category = category;
        this.severity = severity;
        this.description = description;
        this.detectedAt = detectedAt;
        this.resolved = false;
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

    public LaboratoryProcedure getProcedure() {
        return procedure;
    }

    public void setProcedure(LaboratoryProcedure procedure) {
        this.procedure = procedure;
    }

    public QualitySeverity getSeverity() {
        return severity;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getDetectedAt() {
        return detectedAt;
    }

    public boolean isResolved() {
        return resolved;
    }

    public OffsetDateTime getResolvedAt() {
        return resolvedAt;
    }

    public String getCorrectiveAction() {
        return correctiveAction;
    }

    public void markResolved(OffsetDateTime resolvedAt, String correctiveAction) {
        this.resolved = true;
        this.resolvedAt = resolvedAt;
        this.correctiveAction = correctiveAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QualityAlert that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
