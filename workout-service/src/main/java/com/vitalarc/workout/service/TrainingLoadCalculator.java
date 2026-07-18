package com.vitalarc.workout.service;

import com.vitalarc.workout.dto.TrainingLoadResponse;
import com.vitalarc.workout.entity.Workout;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TrainingLoadCalculator {

    private static final int ACUTE_WINDOW_DAYS = 7;
    private static final int CHRONIC_WINDOW_DAYS = 28;

    public TrainingLoadResponse calculate(List<Workout> recentWorkouts, LocalDate asOfDate) {
        double acuteLoad = averageDailyLoad(recentWorkouts, asOfDate, ACUTE_WINDOW_DAYS);
        double chronicLoad = averageDailyLoad(recentWorkouts, asOfDate, CHRONIC_WINDOW_DAYS);
        double acwr = chronicLoad == 0 ? 0.0 : acuteLoad / chronicLoad;

        return new TrainingLoadResponse(round(acuteLoad), round(chronicLoad), round(acwr), classify(acwr));
    }

    private double averageDailyLoad(List<Workout> workouts, LocalDate asOfDate, int windowDays) {
        LocalDate windowStart = asOfDate.minusDays(windowDays - 1L);
        double totalLoad = workouts.stream()
                .filter(w -> !w.getWorkoutDate().isBefore(windowStart) && !w.getWorkoutDate().isAfter(asOfDate))
                .mapToInt(Workout::getLoad)
                .sum();
        return totalLoad / (double) windowDays;
    }

    private String classify(double acwr) {
        if (acwr == 0.0) return "NO_DATA";
        if (acwr < 0.8) return "UNDERTRAINING";
        if (acwr <= 1.3) return "SWEET_SPOT";
        if (acwr <= 1.5) return "CAUTION";
        return "HIGH_RISK";
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}