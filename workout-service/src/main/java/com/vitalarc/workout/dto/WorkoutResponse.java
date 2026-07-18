package com.vitalarc.workout.dto;

import com.vitalarc.workout.entity.Workout;
import java.time.LocalDate;
import java.util.UUID;

public record WorkoutResponse(
        UUID id,
        LocalDate workoutDate,
        String activityType,
        Integer durationMinutes,
        Integer rpe,
        Integer load
) {
    public static WorkoutResponse from(Workout workout) {
        return new WorkoutResponse(
                workout.getId(),
                workout.getWorkoutDate(),
                workout.getActivityType(),
                workout.getDurationMinutes(),
                workout.getRpe(),
                workout.getLoad()
        );
    }
}