package com.example.ivf.dto;

import com.example.ivf.domain.enums.EmbryoGrade;
import com.example.ivf.domain.enums.EmbryoStage;
import com.example.ivf.domain.enums.EmbryoStatus;
import java.time.OffsetDateTime;

public record UpdateEmbryoRequest(
    EmbryoStage stage,
    EmbryoGrade grade,
    EmbryoStatus status,
    OffsetDateTime observationTime
) {
}
