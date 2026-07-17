package com.vitalarc.workout.service;

import com.vitalarc.workout.entity.Workout;
import com.vitalarc.workout.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final WorkoutRepository repository;

    public Workout logWorkout(Workout workout) {
        return repository.save(workout);
    }

    public List<Workout> getWorkoutsForUser(Long userId, LocalDate start, LocalDate end) {
        return repository.findByUserIdAndDateBetween(userId, start, end);
    }
}