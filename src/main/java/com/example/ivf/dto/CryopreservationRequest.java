package com.example.ivf.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

public record CryopreservationRequest(
    @NotBlank String facility,
    @NotBlank String tank,
    @NotBlank String canister,
    @NotBlank String position,
    OffsetDateTime cryoDate
) {
}
