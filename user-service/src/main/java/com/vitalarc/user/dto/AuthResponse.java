package com.vitalarc.user.dto;

/**
 * Never include passwordHash here or anywhere in the DTO layer -
 * entities and API contracts are deliberately kept separate.
 */
public record AuthResponse(
        String accessToken,
        long expiresInSeconds,
        UserResponse user
) {
}
