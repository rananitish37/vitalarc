package com.vitalarc.workout.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "workouts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workout {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID userId;

    @Column(nullable = false)
    private LocalDate workoutDate;

    @Column(nullable = false, length = 100)
    private String activityType;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private Integer rpe;

    @Column(name = "`load`")
    private Integer load;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Workout(UUID userId, LocalDate workoutDate, String activityType, Integer durationMinutes, Integer rpe) {
        this.userId = userId;
        this.workoutDate = workoutDate;
        this.activityType = activityType;
        this.durationMinutes = durationMinutes;
        this.rpe = rpe;
        this.load = durationMinutes * rpe;
    }
}