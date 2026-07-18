package com.vitalarc.coach.dto;

import com.vitalarc.coach.entity.Recommendation;

import java.time.Instant;

public record RecommendationResponse(
        String recommendationText,
        double acwr,
        String riskZone,
        Instant generatedAt
) {
    public static RecommendationResponse from(Recommendation r) {
        return new RecommendationResponse(r.getRecommendationText(), r.getAcwrSnapshot(), r.getRiskZoneSnapshot(), r.getCreatedAt());
    }
}