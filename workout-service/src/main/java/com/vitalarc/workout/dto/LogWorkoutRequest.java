package com.vitalarc.workout.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record LogWorkoutRequest(
        @NotNull LocalDate workoutDate,
        @NotBlank @Size(max = 100) String activityType,
        @NotNull @Min(1) @Max(600) Integer durationMinutes,
        @NotNull @Min(1) @Max(10) Integer rpe
) {
}