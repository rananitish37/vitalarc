package com.vitalarc.reporting.repository;

import com.vitalarc.reporting.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {

    List<Report> findByUserIdOrderByCreatedAtDesc(UUID userId);
}