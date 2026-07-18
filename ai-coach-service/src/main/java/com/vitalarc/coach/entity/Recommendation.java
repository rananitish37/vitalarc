package com.vitalarc.coach.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

/**
 * One AI-generated recommendation, snapshotting the training-load numbers it was
 * based on. Storing the snapshot (not just the text) matters: if someone asks "why
 * did the coach say that," we can show the exact ACWR/riskZone that drove it,
 * instead of the reasoning being an unreproducible black box.
 */
@Entity
@Table(name = "recommendations", indexes = {
        @Index(name = "idx_recommendations_user_created", columnList = "userId, createdAt")
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommendation {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String recommendationText;

    @Column(nullable = false)
    private Double acwrSnapshot;

    @Column(nullable = false, length = 30)
    private String riskZoneSnapshot;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Recommendation(UUID userId, String recommendationText, Double acwrSnapshot, String riskZoneSnapshot) {
        this.userId = userId;
        this.recommendationText = recommendationText;
        this.acwrSnapshot = acwrSnapshot;
        this.riskZoneSnapshot = riskZoneSnapshot;
    }
}