package com.echofyteam.backend.feature.flashcard.entity;

import com.echofyteam.backend.feature.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "user_flashcard_progresses",
        indexes = {
                @Index(name = "idx_user_flashcard", columnList = "user_id, flashcard_id"),
                @Index(name = "idx_user_id", columnList = "user_id"),
                @Index(name = "idx_flashcard_id", columnList = "flashcard_id")
        }
)
public class UserFlashcardProgressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flashcard_id", nullable = false)
    private FlashcardEntity flashcard;

    @Enumerated(EnumType.STRING)
    @Column(name = "learning_level", nullable = false)
    private FlashcardLearningLevel learningLevel;

    @Column(name = "repetition_count", nullable = false)
    private int repetitionCount;

    @Column(name = "last_reviewed_at")
    private Instant lastReviewedAt;
}
