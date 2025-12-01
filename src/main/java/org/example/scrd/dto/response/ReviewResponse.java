package org.example.scrd.dto.response;

import lombok.*;
import org.example.scrd.domain.PerfactoReview;

import java.time.LocalDateTime;

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
    private UserSimpleResponse user;
    private Double rating;
    private String content;
    private Integer helpfulCount;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    /**
     * Entity를 Response DTO로 변환
     */
    public static ReviewResponse from(PerfactoReview review) {
        return ReviewResponse.builder()
            .id(review.getId())
            .placeId(review.getPlace().getId())
            .placeName(review.getPlace().getName())
            .user(UserSimpleResponse.from(review.getUser()))
            .rating(review.getRating())
            .content(review.getContent())
            .helpfulCount(review.getHelpfulCount())
            .regDate(review.getRegDate())
            .modDate(review.getModDate())
            .build();
    }
}
