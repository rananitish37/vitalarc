package com.vitalarc.workout.repository;

import com.vitalarc.workout.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface WorkoutRepository extends JpaRepository<Workout, UUID> {

    List<Workout> findByUserIdAndWorkoutDateBetween(UUID userId, LocalDate start, LocalDate end);

    List<Workout> findByUserIdOrderByWorkoutDateDesc(UUID userId);
}