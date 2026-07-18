package com.vitalarc.coach.client;

/**
 * The seam between our business logic and whichever LLM provider we use.
 * CoachService depends on THIS, never on a concrete provider - so swapping
 * providers (as we're doing right now, Anthropic -> Gemini, due to budget)
 * means writing one new class, not touching CoachService at all.
 */
public interface AiClient {
    String generate(String systemPrompt, String userMessage);
}