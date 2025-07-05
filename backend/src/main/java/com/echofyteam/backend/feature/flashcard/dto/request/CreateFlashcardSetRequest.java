package com.echofyteam.backend.feature.flashcard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * DTO for creating a new FlashcardEntity Set.
 *
 * @param title      the title of the flashcard set (required)
 * @param previewUrl the URL of the preview image (optional)
 * @param publicFlag the publicity
 */
@Builder
@Schema(name = "CreateFlashcardSetRequest", description = "Request payload for creating a flashcard set")
public record CreateFlashcardSetRequest(
        @NotBlank(message = "Title must not be blank")
        @Schema(description = "Title of the flashcard set", example = "Basic English Vocabulary", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "URL of the preview image", example = "https://example.com/images/preview.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String previewUrl,

        @Schema(description = "Publicity of the flashcard set", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean publicFlag
) {}