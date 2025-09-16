package com.example.ivf.domain.value;

import com.example.ivf.domain.enums.ChecklistStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class ChecklistItem {

    @NotBlank
    @Column(name = "check_name")
    private String name;

    @Enumerated(EnumType.STRING)
    private ChecklistStatus status;

    private String comment;

    protected ChecklistItem() {
        // for JPA
    }

    public ChecklistItem(String name, @NotNull ChecklistStatus status, String comment) {
        this.name = name;
        this.status = status;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public ChecklistStatus getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }
}
