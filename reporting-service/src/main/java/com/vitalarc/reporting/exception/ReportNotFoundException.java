package com.vitalarc.reporting.exception;

import java.util.UUID;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException(UUID reportId) {
        super("No report found with id '%s' for this user".formatted(reportId));
    }
}