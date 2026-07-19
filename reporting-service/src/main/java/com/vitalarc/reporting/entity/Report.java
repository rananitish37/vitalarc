package com.vitalarc.reporting.entity;

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

/**
 * Stores metadata about a generated report, NOT the PDF bytes themselves - those live
 * in whatever ReportStorage implementation is active (local disk for now, S3 later).
 * Keeping the database record separate from the file is deliberate: it means we can
 * migrate storage backends later without touching this table at all.
 */
@Entity
@Table(name = "reports", indexes = {
        @Index(name = "idx_reports_user_created", columnList = "userId, createdAt")
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID userId;

    @Column(nullable = false)
    private LocalDate weekStartDate;

    @Column(nullable = false)
    private LocalDate weekEndDate;

    // Storage-agnostic reference: a local file path today, an S3 key/URL tomorrow -
    // the ReportStorage implementation is what interprets this string.
    @Column(nullable = false, length = 500)
    private String storageReference;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Report(UUID userId, LocalDate weekStartDate, LocalDate weekEndDate, String storageReference) {
        this.userId = userId;
        this.weekStartDate = weekStartDate;
        this.weekEndDate = weekEndDate;
        this.storageReference = storageReference;
    }
}