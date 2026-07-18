package com.vitalarc.coach.repository;

import com.vitalarc.coach.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRepository extends JpaRepository<Recommendation, UUID> {

    Optional<Recommendation> findFirstByUserIdOrderByCreatedAtDesc(UUID userId);
}