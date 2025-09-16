package com.example.ivf.domain;

import com.example.ivf.domain.enums.EmbryoGrade;
import com.example.ivf.domain.enums.EmbryoStage;
import com.example.ivf.domain.enums.EmbryoStatus;
import com.example.ivf.domain.value.CryoStorageLocation;
import jakarta.persistence.Embedded;
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
@Table(name = "embryo_records")
public class EmbryoRecord {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id")
    private TreatmentCycle cycle;

    private int embryoNumber;

    @Enumerated(EnumType.STRING)
    private EmbryoStage stage;

    @Enumerated(EnumType.STRING)
    private EmbryoGrade grade;

    @Enumerated(EnumType.STRING)
    private EmbryoStatus status;

    private OffsetDateTime observationTime;

    private boolean cryopreserved;

    private OffsetDateTime cryoDate;

    @Embedded
    private CryoStorageLocation cryoLocation;

    protected EmbryoRecord() {
        // for JPA
    }

    public EmbryoRecord(int embryoNumber, EmbryoStage stage, EmbryoGrade grade, EmbryoStatus status, OffsetDateTime observationTime) {
        this.embryoNumber = embryoNumber;
        this.stage = stage;
        this.grade = grade;
        this.status = status;
        this.observationTime = observationTime;
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

    public int getEmbryoNumber() {
        return embryoNumber;
    }

    public EmbryoStage getStage() {
        return stage;
    }

    public void setStage(EmbryoStage stage) {
        this.stage = stage;
    }

    public EmbryoGrade getGrade() {
        return grade;
    }

    public void setGrade(EmbryoGrade grade) {
        this.grade = grade;
    }

    public EmbryoStatus getStatus() {
        return status;
    }

    public void setStatus(EmbryoStatus status) {
        this.status = status;
    }

    public OffsetDateTime getObservationTime() {
        return observationTime;
    }

    public void setObservationTime(OffsetDateTime observationTime) {
        this.observationTime = observationTime;
    }

    public boolean isCryopreserved() {
        return cryopreserved;
    }

    public OffsetDateTime getCryoDate() {
        return cryoDate;
    }

    public void markCryopreserved(CryoStorageLocation location, OffsetDateTime cryoDate) {
        this.cryopreserved = true;
        this.status = EmbryoStatus.CRYOPRESERVED;
        this.cryoLocation = location;
        this.cryoDate = cryoDate;
    }

    public CryoStorageLocation getCryoLocation() {
        return cryoLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmbryoRecord that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
