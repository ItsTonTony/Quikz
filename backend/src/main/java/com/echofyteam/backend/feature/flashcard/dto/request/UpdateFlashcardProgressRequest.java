package com.echofyteam.backend.feature.flashcard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * DTO for updating the learning progress of a flashcard.
 *
 * @param isUnsure indicates whether the user is unsure about the flashcard (true if unsure, false if confident)
 */
@Builder
@Schema(name = "UpdateFlashcardProgressRequest", description = "Request payload to update flashcard learning progress")
public record UpdateFlashcardProgressRequest(
        @Schema(description = "Flag indicating if the user is unsure about the flashcard", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean isUnsure
) {}