package org.example.scrd.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * 리뷰 생성 요청 DTO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequest {

    @NotNull(message = "장소 ID는 필수입니다")
    private Long placeId;

    @NotNull(message = "평점은 필수입니다")
    @DecimalMin(value = "1.0", message = "평점은 1.0 이상이어야 합니다")
    @DecimalMax(value = "5.0", message = "평점은 5.0 이하여야 합니다")
    private Double rating;

    @NotBlank(message = "리뷰 내용은 필수입니다")
    @Size(min = 10, max = 2000, message = "리뷰 내용은 10자 이상 2000자 이하여야 합니다")
    private String content;
}
