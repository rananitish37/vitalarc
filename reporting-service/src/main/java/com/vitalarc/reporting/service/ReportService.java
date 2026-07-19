package com.vitalarc.reporting.service;

import com.vitalarc.reporting.client.AiCoachServiceClient;
import com.vitalarc.reporting.client.WorkoutServiceClient;
import com.vitalarc.reporting.dto.ReportResponse;
import com.vitalarc.reporting.entity.Report;
import com.vitalarc.reporting.exception.ReportNotFoundException;
import com.vitalarc.reporting.repository.ReportRepository;
import com.vitalarc.reporting.storage.ReportStorage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReportService {

    private final WorkoutServiceClient workoutServiceClient;
    private final AiCoachServiceClient aiCoachServiceClient;
    private final PdfReportBuilder pdfReportBuilder;
    private final ReportStorage reportStorage;
    private final ReportRepository reportRepository;

    public ReportService(WorkoutServiceClient workoutServiceClient,
                         AiCoachServiceClient aiCoachServiceClient,
                         PdfReportBuilder pdfReportBuilder,
                         ReportStorage reportStorage,
                         ReportRepository reportRepository) {
        this.workoutServiceClient = workoutServiceClient;
        this.aiCoachServiceClient = aiCoachServiceClient;
        this.pdfReportBuilder = pdfReportBuilder;
        this.reportStorage = reportStorage;
        this.reportRepository = reportRepository;
    }

    public ReportResponse generateReport(UUID userId) {
        LocalDate weekEnd = LocalDate.now();
        LocalDate weekStart = weekEnd.minusDays(6);

        var trainingLoad = workoutServiceClient.getTrainingLoad(userId);
        var recommendation = aiCoachServiceClient.getLatestRecommendation(userId);

        byte[] pdfBytes = pdfReportBuilder.build(weekStart, weekEnd, trainingLoad, recommendation);

        String fileName = "report-%s-%s.pdf".formatted(userId, weekEnd);
        String storageReference = reportStorage.store(fileName, pdfBytes);

        Report report = new Report(userId, weekStart, weekEnd, storageReference);
        reportRepository.save(report);

        return ReportResponse.from(report);
    }

    public List<ReportResponse> getHistory(UUID userId) {
        return reportRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(ReportResponse::from)
                .toList();
    }

    public byte[] downloadReport(UUID userId, UUID reportId) {
        Report report = reportRepository.findById(reportId)
                .filter(r -> r.getUserId().equals(userId)) // never let one user download another's report
                .orElseThrow(() -> new ReportNotFoundException(reportId));

        return reportStorage.retrieve(report.getStorageReference());
    }
}