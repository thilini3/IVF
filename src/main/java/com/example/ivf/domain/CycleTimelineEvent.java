package com.example.ivf.domain;

import com.example.ivf.domain.enums.TimelineEventType;
import com.example.ivf.domain.enums.TimelineStatus;
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
@Table(name = "cycle_timeline_events")
public class CycleTimelineEvent {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id")
    private TreatmentCycle cycle;

    @Enumerated(EnumType.STRING)
    private TimelineEventType eventType;

    @Column(nullable = false)
    private String description;

    private OffsetDateTime scheduledAt;

    private OffsetDateTime completedAt;

    @Enumerated(EnumType.STRING)
    private TimelineStatus status;

    private String notes;

    protected CycleTimelineEvent() {
        // for JPA
    }

    public CycleTimelineEvent(TimelineEventType eventType, String description, OffsetDateTime scheduledAt, TimelineStatus status, String notes) {
        this.eventType = eventType;
        this.description = description;
        this.scheduledAt = scheduledAt;
        this.status = status;
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

    public TimelineEventType getEventType() {
        return eventType;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getScheduledAt() {
        return scheduledAt;
    }

    public OffsetDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(OffsetDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public TimelineStatus getStatus() {
        return status;
    }

    public void setStatus(TimelineStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CycleTimelineEvent that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
