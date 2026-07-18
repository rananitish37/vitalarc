package com.vitalarc.workout.dto;

public record TrainingLoadResponse(
        double acuteLoad,
        double chronicLoad,
        double acwr,
        String riskZone
) {
}