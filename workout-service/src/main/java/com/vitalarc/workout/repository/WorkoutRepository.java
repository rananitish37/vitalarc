package com.vitalarc.workout.repository;

import com.vitalarc.workout.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    // Spring Data JPA will automatically implement this query based on the method name
    List<Workout> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}