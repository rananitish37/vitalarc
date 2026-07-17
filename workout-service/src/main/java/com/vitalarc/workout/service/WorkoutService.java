package com.vitalarc.workout.service;

import com.vitalarc.workout.entity.Workout;
import com.vitalarc.workout.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository repository;

    @Transactional
    public Workout logWorkout(Workout workout) {
        return repository.save(workout);
    }

    public List<Workout> getWorkouts(UUID userId, LocalDate start, LocalDate end) {
        return repository.findByUserIdAndWorkoutDateBetween(userId, start, end);
    }

    public List<Workout> getWorkoutHistory(UUID userId) {
        return repository.findByUserIdOrderByWorkoutDateDesc(userId);
    }
}