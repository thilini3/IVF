package com.example.ivf.dto;

public record CryoStorageLocationResponse(
    String facility,
    String tank,
    String canister,
    String position
) {
}
