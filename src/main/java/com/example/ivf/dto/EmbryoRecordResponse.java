package com.example.ivf.dto;

import com.example.ivf.domain.enums.EmbryoGrade;
import com.example.ivf.domain.enums.EmbryoStage;
import com.example.ivf.domain.enums.EmbryoStatus;
import java.time.OffsetDateTime;
import java.util.UUID;

public record EmbryoRecordResponse(
    UUID id,
    int embryoNumber,
    EmbryoStage stage,
    EmbryoGrade grade,
    EmbryoStatus status,
    OffsetDateTime observationTime,
    boolean cryopreserved,
    OffsetDateTime cryoDate,
    CryoStorageLocationResponse cryoLocation
) {
}
