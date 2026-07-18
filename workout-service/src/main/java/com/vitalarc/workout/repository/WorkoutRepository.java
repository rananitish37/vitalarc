package com.vitalarc.workout.repository;

import com.vitalarc.workout.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface WorkoutRepository extends JpaRepository<Workout, UUID> {

    List<Workout> findByUserIdOrderByWorkoutDateDesc(UUID userId);

    @Query("SELECT w FROM Workout w WHERE w.userId = :userId AND w.workoutDate BETWEEN :from AND :to")
    List<Workout> findByUserIdAndDateRange(
            @Param("userId") UUID userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}