package com.vitalarc.reporting.dto;

public record RecommendationSnapshot(
        String recommendationText,
        double acwr,
        String riskZone
) {
}