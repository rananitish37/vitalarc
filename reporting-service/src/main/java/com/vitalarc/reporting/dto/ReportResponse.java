package com.vitalarc.reporting.dto;

import com.vitalarc.reporting.entity.Report;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ReportResponse(
        UUID id,
        LocalDate weekStartDate,
        LocalDate weekEndDate,
        Instant createdAt
) {
    public static ReportResponse from(Report report) {
        return new ReportResponse(report.getId(), report.getWeekStartDate(), report.getWeekEndDate(), report.getCreatedAt());
    }
}