package com.echofyteam.backend.feature.flashcard.controller;

import com.echofyteam.backend.feature.flashcard.dto.request.CreateFlashcardSetRequest;
import com.echofyteam.backend.feature.flashcard.dto.request.UpdateFlashcardSetRequest;
import com.echofyteam.backend.feature.flashcard.dto.response.FlashcardSetResponse;
import com.echofyteam.backend.feature.flashcard.service.FlashcardSetService;
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

import java.util.UUID;

@Tag(name = "FlashcardEntity Sets", description = "API for managing flashcard sets")
@RestController
@RequestMapping("/api/v1/flashcard-sets")
@RequiredArgsConstructor
public class FlashcardSetController {
    private final FlashcardSetService flashcardSetService;

    @Operation(
            summary = "Get all public flashcard sets",
            description = "Retrieve a paginated list of all public flashcard sets",
            responses = {
                    @ApiResponse(responseCode = "200", description = "FlashcardEntity sets retrieved successfully",
                            content = @Content(schema = @Schema(implementation = FlashcardSetResponse.class)))
            }
    )
    @GetMapping("/public")
    public ResponseEntity<Page<FlashcardSetResponse>> getAllPublicFlashcardSets(
            @Parameter(description = "Pagination information", required = false)
            Pageable pageable) {
        Page<FlashcardSetResponse> flashcardSetResponses =
                flashcardSetService.getAllPublicFlashcardSets(pageable);

        return ResponseEntity.ok(flashcardSetResponses);
    }

    @Operation(
            summary = "Get all personal flashcard sets",
            description = "Retrieve a paginated list of all personal flashcard sets",
            responses = {
                    @ApiResponse(responseCode = "200", description = "FlashcardEntity sets retrieved successfully",
                            content = @Content(schema = @Schema(implementation = FlashcardSetResponse.class)))
            }
    )
    @GetMapping("/personal")
    public ResponseEntity<Page<FlashcardSetResponse>> getAllPersonalFlashcardSets(
            @Parameter(description = "Pagination information", required = false)
            Pageable pageable) {
        Page<FlashcardSetResponse> flashcardSetResponses =
                flashcardSetService.getAllPersonalFlashcardSets(pageable);

        return ResponseEntity.ok(flashcardSetResponses);
    }

    @Operation(
            summary = "Get flashcard set by ID",
            description = "Retrieve a flashcard set by its UUID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "FlashcardEntity set retrieved successfully",
                            content = @Content(schema = @Schema(implementation = FlashcardSetResponse.class))),
                    @ApiResponse(responseCode = "404", description = "FlashcardEntity set not found")
            }
    )
    @GetMapping("/{flashcardSetID}")
    public ResponseEntity<FlashcardSetResponse> getFlashcardSetByID(
            @Parameter(description = "UUID of the flashcard set", required = true)
            @PathVariable UUID flashcardSetID
    ) {
        FlashcardSetResponse flashcardSetResponse =
                flashcardSetService.getFlashcardSetByID(flashcardSetID);

        return ResponseEntity.ok(flashcardSetResponse);
    }

    @Operation(
            summary = "Create a new flashcard set",
            description = "Create a new flashcard set with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "FlashcardEntity set created successfully",
                            content = @Content(schema = @Schema(implementation = FlashcardSetResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping
    public ResponseEntity<FlashcardSetResponse> createFlashcardSet(
            @Parameter(description = "FlashcardEntity set creation payload", required = true)
            @RequestBody @Valid CreateFlashcardSetRequest createFlashcardSetRequest
    ) {
        FlashcardSetResponse flashcardSetResponse =
                flashcardSetService.createFlashcardSet(createFlashcardSetRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(flashcardSetResponse);
    }

    @Operation(
            summary = "Partially update a flashcard set",
            description = "Partially update a flashcard set by its UUID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "FlashcardEntity set updated successfully",
                            content = @Content(schema = @Schema(implementation = FlashcardSetResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "FlashcardEntity set not found")
            }
    )
    @PatchMapping("/{flashcardSetID}")
    public ResponseEntity<FlashcardSetResponse> updateFlashcardSet(
            @Parameter(description = "UUID of the flashcard set", required = true)
            @PathVariable UUID flashcardSetID,
            @Parameter(description = "Partial flashcard set update payload", required = true)
            @RequestBody @Valid UpdateFlashcardSetRequest updateFlashcardSetRequest
    ) {
        FlashcardSetResponse flashcardResponse =
                flashcardSetService.updateFlashcardSet(flashcardSetID, updateFlashcardSetRequest);

        return ResponseEntity.ok(flashcardResponse);
    }

    @Operation(
            summary = "Delete a flashcard set",
            description = "Delete a flashcard set by its UUID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "FlashcardEntity set deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "FlashcardEntity set not found")
            }
    )
    @DeleteMapping("/{flashcardSetID}")
    public ResponseEntity<Void> deleteFlashcardSet(
            @Parameter(description = "UUID of the flashcard set", required = true)
            @PathVariable UUID flashcardSetID
    ) {
        flashcardSetService.deleteFlashcardSet(flashcardSetID);

        return ResponseEntity.noContent().build();
    }
}
