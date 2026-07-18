package com.vitalarc.coach.client;

import com.vitalarc.coach.dto.TrainingLoadSnapshot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

/**
 * Internal service-to-service call, deliberately going DIRECTLY to workout-service
 * rather than back out through the gateway. The gateway's job is authenticating
 * external client requests; internal service calls don't need a JWT re-check -
 * they run on a private network in production (inside the same VPC/ECS cluster),
 * so we just forward the user id we already trust.
 */
@Component
public class TrainingLoadClient {

    private final RestClient restClient;

    public TrainingLoadClient(@Value("${services.workout-service.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public TrainingLoadSnapshot getTrainingLoad(UUID userId) {
        return restClient.get()
                .uri("/api/workouts/training-load")
                .header("X-User-Id", userId.toString())
                .retrieve()
                .body(TrainingLoadSnapshot.class);
    }
}