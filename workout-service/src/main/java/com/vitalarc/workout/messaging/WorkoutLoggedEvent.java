package com.vitalarc.workout.messaging;

import java.time.Instant;
import java.util.UUID;

public record WorkoutLoggedEvent(UUID userId, UUID workoutId, Instant occurredAt) {
}