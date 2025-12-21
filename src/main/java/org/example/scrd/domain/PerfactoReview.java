package org.example.scrd.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.scrd.BaseEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * 리뷰 엔티티 (Perfacto용 - 피벗팅 버전)
 * 사용자가 장소에 대해 작성한 3단계 리뷰
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
        @Index(name = "idx_overall_rating", columnList = "overall_rating")
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

    // 1단계: 전체 평가 (신호등 방식)
    @Enumerated(EnumType.STRING)
    @Column(name = "overall_rating", nullable = false, length = 20)
    private ReviewRating overallRating; // GOOD(좋았음), NEUTRAL(보통), BAD(별로임)

    // 2단계: 이유 선택 (다중 선택, 콤마로 구분하여 저장)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "review_reasons", joinColumns = @JoinColumn(name = "review_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "reason", length = 50)
    @Builder.Default
    private Set<ReviewReason> reasons = new HashSet<>();

    // 3단계: 카테고리 비교 (선택적)
    @Column(name = "compared_place_id")
    private Long comparedPlaceId; // 비교 대상 장소 ID (null 가능)

    @Enumerated(EnumType.STRING)
    @Column(name = "comparison_result", length = 20)
    private ComparisonResult comparison; // BETTER, SIMILAR, WORSE (null 가능)

    // 소셜 기능
    @Builder.Default
    @Column(nullable = false)
    private Integer likeCount = 0; // 좋아요 수

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true; // 활성화 여부 (삭제된 리뷰는 false)

    /**
     * 정적 팩토리 메서드
     */
    public static PerfactoReview create(
            Place place,
            User user,
            ReviewRating overallRating,
            Set<ReviewReason> reasons,
            Long comparedPlaceId,
            ComparisonResult comparison
    ) {
        return PerfactoReview.builder()
            .place(place)
            .user(user)
            .overallRating(overallRating)
            .reasons(reasons != null ? reasons : new HashSet<>())
            .comparedPlaceId(comparedPlaceId)
            .comparison(comparison)
            .likeCount(0)
            .isActive(true)
            .build();
    }

    /**
     * 좋아요 증가
     */
    public void incrementLikeCount() {
        this.likeCount++;
    }

    /**
     * 좋아요 감소
     */
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    /**
     * 평점을 숫자로 반환 (평균 계산용)
     */
    public int getNumericRating() {
        return overallRating.getNumericValue();
    }

    /**
     * 기존 코드 호환성을 위한 메서드
     */
    public Double getRating() {
        return (double) overallRating.getNumericValue();
    }

    public String getContent() {
        // 이유들을 문자열로 변환
        return reasons.stream()
            .map(r -> r.getDescription())
            .reduce((a, b) -> a + ", " + b)
            .orElse("");
    }

    public Integer getHelpfulCount() {
        return likeCount;
    }

    public void incrementHelpfulCount() {
        incrementLikeCount();
    }
}
