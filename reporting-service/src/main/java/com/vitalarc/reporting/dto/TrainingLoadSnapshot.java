package com.vitalarc.reporting.dto;

public record TrainingLoadSnapshot(
        double acuteLoad,
        double chronicLoad,
        double acwr,
        String riskZone
) {
}