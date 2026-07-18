package com.vitalarc.workout.controller;

import com.vitalarc.workout.dto.LogWorkoutRequest;
import com.vitalarc.workout.dto.TrainingLoadResponse;
import com.vitalarc.workout.dto.WorkoutResponse;
import com.vitalarc.workout.service.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * X-User-Id is never trusted from the client directly - it's set by the gateway's
 * JwtAuthenticationFilter after verifying the JWT, so by the time a request reaches
 * this controller, that header is already a validated fact, not user input.
 */
@RestController
@RequestMapping("/api/workouts")
@Tag(name = "Workouts", description = "Logging workouts and reading training load")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    @Operation(summary = "Log a new workout")
    public ResponseEntity<WorkoutResponse> logWorkout(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody LogWorkoutRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutService.logWorkout(userId, request));
    }

    @GetMapping
    @Operation(summary = "Get this user's workout history, most recent first")
    public ResponseEntity<List<WorkoutResponse>> getHistory(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(workoutService.getHistory(userId));
    }

    @GetMapping("/training-load")
    @Operation(summary = "Get the current ACWR training load snapshot")
    public ResponseEntity<TrainingLoadResponse> getTrainingLoad(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(workoutService.getCurrentTrainingLoad(userId));
    }
}