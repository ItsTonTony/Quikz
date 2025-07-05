package com.echofyteam.backend.feature.flashcard.controller;

import com.echofyteam.backend.feature.flashcard.dto.request.CreateFlashcardRequest;
import com.echofyteam.backend.feature.flashcard.dto.request.UpdateFlashcardProgressRequest;
import com.echofyteam.backend.feature.flashcard.dto.request.UpdateFlashcardRequest;
import com.echofyteam.backend.feature.flashcard.dto.response.FlashcardResponse;
import com.echofyteam.backend.feature.flashcard.dto.response.UserFlashcardProgressResponse;
import com.echofyteam.backend.feature.flashcard.service.FlashcardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Flashcards", description = "API for managing flashcards within flashcard sets")
@RestController
@RequestMapping("/api/v1/flashcard-sets/{flashcardSetID}/flashcards")
@RequiredArgsConstructor
public class FlashcardController {
    private final FlashcardService flashcardService;

    @Operation(
            summary = "Get all flashcards",
            description = "Retrieve a paginated list of all flashcards in the specified flashcard set",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of flashcards retrieved successfully",
                            content = @Content(schema = @Schema(implementation = FlashcardResponse.class)))
            }
    )
    @GetMapping
    public ResponseEntity<Page<FlashcardResponse>> getAllFlashcards(
            @Parameter(description = "UUID of the flashcard set", required = true)
            @PathVariable UUID flashcardSetID,
            @Parameter(hidden = true) Pageable pageable
    ) {
        Page<FlashcardResponse> flashcardResponses =
                flashcardService.getAllFlashcards(flashcardSetID, pageable);

        return ResponseEntity.ok(flashcardResponses);
    }

    @Operation(
            summary = "Get flashcard by ID",
            description = "Retrieve a specific flashcard by its ID within the given flashcard set",
            responses = {
                    @ApiResponse(responseCode = "200", description = "FlashcardEntity retrieved successfully",
                            content = @Content(schema = @Schema(implementation = FlashcardResponse.class))),
                    @ApiResponse(responseCode = "404", description = "FlashcardEntity or flashcard set not found")
            }
    )
    @GetMapping("/{flashcardID}")
    public ResponseEntity<FlashcardResponse> getFlashcardById(
            @Parameter(description = "UUID of the flashcard set", required = true)
            @PathVariable UUID flashcardSetID,
            @Parameter(description = "UUID of the flashcard", required = true)
            @PathVariable UUID flashcardID
    ) {
        FlashcardResponse flashcardResponse =
                flashcardService.getFlashcardByID(flashcardSetID, flashcardID);

        return ResponseEntity.ok(flashcardResponse);
    }

    @Operation(
            summary = "Get random flashcard",
            description = "Retrieve a random flashcard from the specified flashcard set",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Random flashcard retrieved successfully",
                            content = @Content(schema = @Schema(implementation = FlashcardResponse.class))),
                    @ApiResponse(responseCode = "404", description = "FlashcardEntity set not found or empty")
            }
    )
    @GetMapping("/random")
    public ResponseEntity<FlashcardResponse> getRandomFlashcard(
            @Parameter(description = "UUID of the flashcard set", required = true)
            @PathVariable UUID flashcardSetID
    ) {
        FlashcardResponse flashcardResponse =
                flashcardService.getRandomFlashcard(flashcardSetID);

        return ResponseEntity.ok(flashcardResponse);
    }

    @Operation(
            summary = "Create a new flashcard",
            description = "Create a new flashcard in the specified flashcard set",
            responses = {
                    @ApiResponse(responseCode = "201", description = "FlashcardEntity created successfully",
                            content = @Content(schema = @Schema(implementation = FlashcardResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping
    public ResponseEntity<FlashcardResponse> createFlashcard(
            @Parameter(description = "UUID of the flashcard set", required = true)
            @PathVariable UUID flashcardSetID,
            @Parameter(description = "FlashcardEntity creation payload", required = true)
            @RequestBody @Valid CreateFlashcardRequest createFlashcardRequest
    ) {
        FlashcardResponse flashcardResponse =
                flashcardService.createFlashcard(flashcardSetID, createFlashcardRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(flashcardResponse);
    }

    @Operation(
            summary = "Batch create flashcards",
            description = "Create multiple flashcards in the specified flashcard set",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Flashcards created successfully",
                            content = @Content(schema = @Schema(implementation = FlashcardResponse.class)))
            }
    )
    @PostMapping("/batch")
    public ResponseEntity<List<FlashcardResponse>> batchFlashcards(
            @Parameter(description = "UUID of the flashcard set", required = true)
            @PathVariable UUID flashcardSetID,
            @Parameter(description = "List of flashcard creation payloads", required = true)
            @RequestBody @Valid List<CreateFlashcardRequest> createFlashcardRequests
    ) {
        List<FlashcardResponse> flashcardResponses =
                flashcardService.batchFlashcards(flashcardSetID, createFlashcardRequests);

        return ResponseEntity.status(HttpStatus.CREATED).body(flashcardResponses);
    }

    @Operation(
            summary = "Partially update a flashcard",
            description = "Partially update fields of a flashcard in the specified flashcard set",
            responses = {
                    @ApiResponse(responseCode = "200", description = "FlashcardEntity updated successfully",
                            content = @Content(schema = @Schema(implementation = FlashcardResponse.class))),
                    @ApiResponse(responseCode = "404", description = "FlashcardEntity or flashcard set not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PatchMapping("/{flashcardID}")
    public ResponseEntity<FlashcardResponse> updateFlashcard(
            @Parameter(description = "UUID of the flashcard set", required = true)
            @PathVariable UUID flashcardSetID,
            @Parameter(description = "UUID of the flashcard", required = true)
            @PathVariable UUID flashcardID,
            @Parameter(description = "Partial flashcard update payload", required = true)
            @RequestBody @Valid UpdateFlashcardRequest updateFlashcardRequest
    ) {
        FlashcardResponse flashcardResponse =
                flashcardService.updateFlashcard(flashcardSetID, flashcardID, updateFlashcardRequest);

        return ResponseEntity.ok(flashcardResponse);
    }

    @Operation(
            summary = "Update flashcard progress",
            description = "Update the learning progress of a flashcard for the current user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Progress updated successfully"),
                    @ApiResponse(responseCode = "404", description = "FlashcardEntity or flashcard set not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping("/{flashcardID}/progress")
    public ResponseEntity<UserFlashcardProgressResponse> updateFlashcardProgress(
            @Parameter(description = "UUID of the flashcard set", required = true)
            @PathVariable UUID flashcardSetID,
            @Parameter(description = "UUID of the flashcard", required = true)
            @PathVariable UUID flashcardID,
            @Parameter(description = "FlashcardEntity progress update payload", required = true)
            @RequestBody @Valid UpdateFlashcardProgressRequest updateFlashcardProgressRequest
    ) {
        UserFlashcardProgressResponse userFlashcardProgressResponse =
                flashcardService.updateFlashcardProgress(flashcardSetID, flashcardID, updateFlashcardProgressRequest);

        return ResponseEntity.ok(userFlashcardProgressResponse);
    }

    @Operation(
            summary = "Delete a flashcard",
            description = "Delete a flashcard from the specified flashcard set",
            responses = {
                    @ApiResponse(responseCode = "204", description = "FlashcardEntity deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "FlashcardEntity or flashcard set not found")
            }
    )
    @DeleteMapping("/{flashcardID}")
    public ResponseEntity<Void> deleteFlashcard(
            @Parameter(description = "UUID of the flashcard set", required = true)
            @PathVariable UUID flashcardSetID,
            @Parameter(description = "UUID of the flashcard", required = true)
            @PathVariable UUID flashcardID
    ) {
        flashcardService.deleteFlashcard(flashcardSetID, flashcardID);

        return ResponseEntity.noContent().build();
    }
}
