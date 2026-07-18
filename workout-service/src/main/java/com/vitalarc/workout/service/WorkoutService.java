package com.vitalarc.workout.service;

import com.vitalarc.workout.dto.LogWorkoutRequest;
import com.vitalarc.workout.dto.TrainingLoadResponse;
import com.vitalarc.workout.dto.WorkoutResponse;
import com.vitalarc.workout.entity.Workout;
import com.vitalarc.workout.messaging.WorkoutEventPublisher;
import com.vitalarc.workout.repository.WorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final TrainingLoadCalculator trainingLoadCalculator;
    private final WorkoutEventPublisher eventPublisher;

    public WorkoutService(WorkoutRepository workoutRepository,
                          TrainingLoadCalculator trainingLoadCalculator,
                          WorkoutEventPublisher eventPublisher) {
        this.workoutRepository = workoutRepository;
        this.trainingLoadCalculator = trainingLoadCalculator;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public WorkoutResponse logWorkout(UUID userId, LogWorkoutRequest request) {
        Workout workout = new Workout(userId, request.workoutDate(), request.activityType(),
                request.durationMinutes(), request.rpe());
        workoutRepository.save(workout);
        eventPublisher.publishWorkoutLogged(userId, workout.getId());
        return WorkoutResponse.from(workout);
    }

    public List<WorkoutResponse> getHistory(UUID userId) {
        return workoutRepository.findByUserIdOrderByWorkoutDateDesc(userId).stream()
                .map(WorkoutResponse::from)
                .toList();
    }

    public TrainingLoadResponse getCurrentTrainingLoad(UUID userId) {
        LocalDate today = LocalDate.now();
        LocalDate lookbackStart = today.minusDays(27);
        List<Workout> recentWorkouts = workoutRepository.findByUserIdAndDateRange(userId, lookbackStart, today);
        return trainingLoadCalculator.calculate(recentWorkouts, today);
    }
}