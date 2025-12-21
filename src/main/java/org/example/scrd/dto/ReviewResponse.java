package org.example.scrd.dto;

import lombok.*;
import org.example.scrd.domain.ComparisonResult;
import org.example.scrd.domain.PerfactoReview;
import org.example.scrd.domain.ReviewRating;
import org.example.scrd.domain.ReviewReason;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 리뷰 응답 DTO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    private Long id;
    private Long placeId;
    private String placeName;
    private Long userId;
    private String userName;
    private String userProfileImage;

    // 리뷰 내용
    private ReviewRating overallRating;
    private Set<ReviewReason> reasons;
    private Long comparedPlaceId;
    private String comparedPlaceName;
    private ComparisonResult comparison;

    // 소셜 정보
    private Integer likeCount;
    private Boolean isLikedByMe; // 현재 사용자가 좋아요 했는지

    private LocalDateTime createdAt;

    /**
     * Entity to DTO
     */
    public static ReviewResponse from(PerfactoReview review) {
        return ReviewResponse.builder()
            .id(review.getId())
            .placeId(review.getPlace().getId())
            .placeName(review.getPlace().getName())
            .userId(review.getUser().getId())
            .userName(review.getUser().getName())
            .userProfileImage(review.getUser().getProfileImageUrl())
            .overallRating(review.getOverallRating())
            .reasons(review.getReasons())
            .comparedPlaceId(review.getComparedPlaceId())
            .comparison(review.getComparison())
            .likeCount(review.getLikeCount())
            .isLikedByMe(false) // 별도로 설정 필요
            .createdAt(review.getRegDate())
            .build();
    }

    public static ReviewResponse from(PerfactoReview review, boolean isLikedByMe) {
        ReviewResponse response = from(review);
        response.setIsLikedByMe(isLikedByMe);
        return response;
    }
}
