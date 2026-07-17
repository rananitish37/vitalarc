package com.vitalarc.workout.controller;

import com.vitalarc.workout.entity.Workout;
import com.vitalarc.workout.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<Workout> logWorkout(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestBody Workout workout) {

        // Ensure the workout is associated with the authenticated user
        workout.setUserId(userId);

        // Ensure the load is calculated if not provided
        if (workout.getLoad() == null) {
            workout.setLoad(workout.getDurationMinutes() * workout.getRpe());
        }

        Workout savedWorkout = workoutService.logWorkout(workout);
        return ResponseEntity.ok(savedWorkout);
    }

    @GetMapping
    public ResponseEntity<List<Workout>> getWorkoutHistory(
            @RequestHeader("X-User-Id") UUID userId) {

        return ResponseEntity.ok(workoutService.getWorkoutHistory(userId));
    }
}