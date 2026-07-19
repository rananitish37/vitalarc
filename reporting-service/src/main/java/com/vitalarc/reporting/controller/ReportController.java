package com.vitalarc.reporting.controller;

import com.vitalarc.reporting.dto.ReportResponse;
import com.vitalarc.reporting.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Weekly PDF training reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/generate")
    @Operation(summary = "Generate a new report from this week's data")
    public ReportResponse generate(@RequestHeader("X-User-Id") UUID userId) {
        return reportService.generateReport(userId);
    }

    @GetMapping
    @Operation(summary = "List this user's past reports")
    public List<ReportResponse> getHistory(@RequestHeader("X-User-Id") UUID userId) {
        return reportService.getHistory(userId);
    }

    @GetMapping("/{reportId}/download")
    @Operation(summary = "Download a specific report as a PDF file")
    public ResponseEntity<byte[]> download(@RequestHeader("X-User-Id") UUID userId, @PathVariable UUID reportId) {
        byte[] pdfBytes = reportService.downloadReport(userId, reportId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"vitalarc-report.pdf\"")
                .body(pdfBytes);
    }
}