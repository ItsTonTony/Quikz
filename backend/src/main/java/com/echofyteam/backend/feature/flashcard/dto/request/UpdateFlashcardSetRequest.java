package com.echofyteam.backend.feature.flashcard.dto.request;

import lombok.Builder;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;

/**
 * DTO for partially updating a flashcard set.
 *
 * @param title      optional new title of the flashcard set
 * @param previewUrl optional new URL of the preview image
 * @param publicFlag optional new public flag
 */
@Builder
@Schema(name = "UpdateFlashcardSetRequest", description = "Request payload for partially updating a flashcard set")
public record UpdateFlashcardSetRequest(
        @Schema(description = "Title of the flashcard set", example = "Basic English Vocabulary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Optional<String> title,

        @Schema(description = "URL of the preview image", example = "https://example.com/images/preview.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Optional<String> previewUrl,

        @Schema(description = "Publicity of the flashcard set", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        Optional<Boolean> publicFlag
) {}
