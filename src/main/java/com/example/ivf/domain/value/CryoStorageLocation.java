package com.example.ivf.domain.value;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public class CryoStorageLocation {

    @NotBlank
    private String facility;

    @NotBlank
    private String tank;

    @NotBlank
    private String canister;

    @NotBlank
    private String position;

    protected CryoStorageLocation() {
        // for JPA
    }

    public CryoStorageLocation(String facility, String tank, String canister, String position) {
        this.facility = facility;
        this.tank = tank;
        this.canister = canister;
        this.position = position;
    }

    public String getFacility() {
        return facility;
    }

    public String getTank() {
        return tank;
    }

    public String getCanister() {
        return canister;
    }

    public String getPosition() {
        return position;
    }
}
