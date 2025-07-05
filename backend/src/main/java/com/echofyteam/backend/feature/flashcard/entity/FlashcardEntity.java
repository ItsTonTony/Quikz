package com.echofyteam.backend.feature.flashcard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "flashcards")
public class FlashcardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    private String description;

    private String translation;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false)
    private FlashcardDifficultyLevel difficultyLevel;

    @ElementCollection
    @CollectionTable(
            name = "flashcard_use_examples",
            joinColumns = @JoinColumn(name = "flashcard_id")
    )
    @Column(name = "example")
    @Builder.Default
    private List<String> useExamples = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private FlashcardSetEntity set;
}
