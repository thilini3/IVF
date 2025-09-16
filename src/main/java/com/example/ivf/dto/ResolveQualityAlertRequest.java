package com.example.ivf.dto;

import java.time.OffsetDateTime;

public record ResolveQualityAlertRequest(
    String correctiveAction,
    OffsetDateTime resolvedAt
) {
}
