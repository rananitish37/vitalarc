package com.vitalarc.reporting.client;

import com.vitalarc.reporting.dto.TrainingLoadSnapshot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class WorkoutServiceClient {

    private final RestClient restClient;

    public WorkoutServiceClient(@Value("${services.workout-service.base-url}") String baseUrl) {
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