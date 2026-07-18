package com.vitalarc.workout.messaging;

import java.time.Instant;
import java.util.UUID;

/**
 * The message body itself. Deliberately minimal - just enough for ai-coach-service to
 * know WHO and WHAT, not a full data dump. If ai-coach-service needs more detail later,
 * it should ask workout-service for it directly rather than us bloating every event
 * "just in case" - that keeps services decoupled.
 */
public record WorkoutLoggedEvent(
        UUID userId,
        UUID workoutId,
        Instant occurredAt
) {
}