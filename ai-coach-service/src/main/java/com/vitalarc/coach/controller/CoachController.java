package com.vitalarc.coach.controller;

import com.vitalarc.coach.dto.ChatRequest;
import com.vitalarc.coach.dto.ChatResponse;
import com.vitalarc.coach.dto.RecommendationResponse;
import com.vitalarc.coach.service.CoachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coach")
@Tag(name = "AI Coach", description = "Recommendations and chat, powered by Claude")
public class CoachController {

    private final CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping("/recommendation")
    @Operation(summary = "Get the latest recommendation (generates one on demand if none exists yet)")
    public RecommendationResponse getRecommendation(@RequestHeader("X-User-Id") UUID userId) {
        return coachService.getLatestRecommendation(userId);
    }

    @PostMapping("/chat")
    @Operation(summary = "Ask the AI coach a direct question")
    public ChatResponse chat(@RequestHeader("X-User-Id") UUID userId, @Valid @RequestBody ChatRequest request) {
        return new ChatResponse(coachService.chat(userId, request.message()));
    }
}