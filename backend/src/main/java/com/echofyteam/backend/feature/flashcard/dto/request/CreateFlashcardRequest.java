package com.echofyteam.backend.feature.flashcard.dto.request;

import com.echofyteam.backend.feature.flashcard.entity.FlashcardDifficultyLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

/**
 * DTO for creating a new FlashcardEntity.
 *
 * @param word            the word or phrase of the flashcard (required)
 * @param description     the explanation or meaning of the word (required)
 * @param translation     the translation of the word (optional)
 * @param difficultyLevel the difficulty level of the flashcard (required)
 * @param useExamples     list of example sentences or usages of the word (optional)
 */
@Builder
@Schema(name = "CreateFlashcardRequest", description = "Request payload for creating a flashcard")
public record CreateFlashcardRequest(
        @NotBlank(message = "Word must not be blank")
        @Schema(description = "Word or phrase", example = "apple", requiredMode = Schema.RequiredMode.REQUIRED)
        String word,

        @NotBlank(message = "Description must not be blank")
        @Schema(description = "Explanation or description of the word", example = "A fruit that grows on trees", requiredMode = Schema.RequiredMode.REQUIRED)
        String description,

        @Schema(description = "Translation of the word", example = "яблоко", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String translation,

        @NotNull(message = "Difficulty level must be specified")
        @Schema(description = "Difficulty level of the flashcard", requiredMode = Schema.RequiredMode.REQUIRED)
        FlashcardDifficultyLevel difficultyLevel,

        @Size(max = 10, message = "Use examples list can contain at most 10 items")
        @Schema(description = "List of example usages", example = "[\"I ate an apple.\", \"Apple pie is delicious.\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<@NotBlank(message = "Example sentence must not be blank") String> useExamples
) {}