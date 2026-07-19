package com.vitalarc.reporting.client;

import com.vitalarc.reporting.dto.RecommendationSnapshot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class AiCoachServiceClient {

    private final RestClient restClient;

    public AiCoachServiceClient(@Value("${services.ai-coach-service.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public RecommendationSnapshot getLatestRecommendation(UUID userId) {
        return restClient.get()
                .uri("/api/coach/recommendation")
                .header("X-User-Id", userId.toString())
                .retrieve()
                .body(RecommendationSnapshot.class);
    }
}