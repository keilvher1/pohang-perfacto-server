package org.example.scrd.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.scrd.BaseEntity;

/**
 * 리뷰 엔티티 (Perfacto용)
 * 사용자가 장소에 대해 작성한 리뷰
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "perfacto_reviews",
    indexes = {
        @Index(name = "idx_place_id", columnList = "place_id"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_rating", columnList = "rating")
    }
)
public class PerfactoReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place; // 리뷰 대상 장소

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 리뷰 작성자

    @Column(nullable = false)
    private Double rating; // 평점 (1.0 ~ 5.0)

    @Column(columnDefinition = "TEXT")
    private String content; // 리뷰 내용

    @Builder.Default
    @Column(nullable = false)
    private Integer helpfulCount = 0; // 도움이 됨 수

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true; // 활성화 여부 (삭제된 리뷰는 false)

    /**
     * 평점 검증
     */
    @PrePersist
    @PreUpdate
    private void validateRating() {
        if (rating < 1.0 || rating > 5.0) {
            throw new IllegalArgumentException("평점은 1.0에서 5.0 사이여야 합니다.");
        }
    }

    /**
     * 정적 팩토리 메서드
     */
    public static PerfactoReview create(Place place, User user, Double rating, String content) {
        if (rating < 1.0 || rating > 5.0) {
            throw new IllegalArgumentException("평점은 1.0에서 5.0 사이여야 합니다.");
        }

        return PerfactoReview.builder()
            .place(place)
            .user(user)
            .rating(rating)
            .content(content)
            .helpfulCount(0)
            .isActive(true)
            .build();
    }

    /**
     * 도움이 됨 증가
     */
    public void incrementHelpfulCount() {
        this.helpfulCount++;
    }
}
