package com.vitalarc.coach.dto;

/**
 * Mirrors workout-service's TrainingLoadResponse. Deliberately a separate class,
 * not a shared library dependency between services - each service owns its own
 * view of any data it consumes from another service. If workout-service changes
 * its shape, this is the one place in ai-coach-service that needs updating.
 */
public record TrainingLoadSnapshot(
        double acuteLoad,
        double chronicLoad,
        double acwr,
        String riskZone
) {
}