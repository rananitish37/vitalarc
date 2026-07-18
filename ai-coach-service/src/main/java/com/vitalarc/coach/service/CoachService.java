package com.vitalarc.coach.service;

import com.vitalarc.coach.client.AiClient;
import com.vitalarc.coach.client.TrainingLoadClient;
import com.vitalarc.coach.dto.RecommendationResponse;
import com.vitalarc.coach.dto.TrainingLoadSnapshot;
import com.vitalarc.coach.entity.Recommendation;
import com.vitalarc.coach.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CoachService {

    private static final String SYSTEM_PROMPT = """
            You are an experienced, encouraging strength & conditioning coach reviewing a
            client's training load data. You will be given their Acute:Chronic Workload
            Ratio (ACWR) and a risk zone classification. Give a short (3-4 sentence),
            specific, actionable recommendation. Reference the actual numbers you were
            given. Do not just repeat the risk zone label back - explain what it means
            for their next few days of training in practical terms.
            """;

    private final TrainingLoadClient trainingLoadClient;
    private final AiClient aiClient;
    private final RecommendationRepository recommendationRepository;

    public CoachService(TrainingLoadClient trainingLoadClient,
                        AiClient aiClient,
                        RecommendationRepository recommendationRepository) {
        this.trainingLoadClient = trainingLoadClient;
        this.aiClient = aiClient;
        this.recommendationRepository = recommendationRepository;
    }

    public RecommendationResponse generateRecommendation(UUID userId) {
        TrainingLoadSnapshot snapshot = trainingLoadClient.getTrainingLoad(userId);

        String userMessage = """
                My current acute (7-day) training load average is %.2f.
                My chronic (28-day) training load average is %.2f.
                My ACWR is %.2f, which falls in the "%s" zone.
                What should I focus on for the next few days?
                """.formatted(snapshot.acuteLoad(), snapshot.chronicLoad(), snapshot.acwr(), snapshot.riskZone());

        String recommendationText = aiClient.generate(SYSTEM_PROMPT, userMessage);

        Recommendation recommendation = new Recommendation(userId, recommendationText, snapshot.acwr(), snapshot.riskZone());
        recommendationRepository.save(recommendation);

        return RecommendationResponse.from(recommendation);
    }

    public RecommendationResponse getLatestRecommendation(UUID userId) {
        return recommendationRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
                .map(RecommendationResponse::from)
                .orElseGet(() -> generateRecommendation(userId)); // no recommendation yet - generate one on demand
    }

    public String chat(UUID userId, String message) {
        TrainingLoadSnapshot snapshot = trainingLoadClient.getTrainingLoad(userId);

        String chatSystemPrompt = SYSTEM_PROMPT + """

                The athlete is now asking you a follow-up question directly. Their current
                ACWR is %.2f (%s zone) - use this as context, but answer their actual
                question conversationally, like a real coach texting back.
                """.formatted(snapshot.acwr(), snapshot.riskZone());

        return aiClient.generate(chatSystemPrompt, message);
    }
}