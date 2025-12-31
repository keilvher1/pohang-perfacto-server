package org.example.scrd.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.controller.response.ApiResponse;
import org.example.scrd.dto.request.ReviewCreateRequest;
import org.example.scrd.dto.response.ReviewResponse;
import org.example.scrd.service.PerfactoReviewService;
import org.example.scrd.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 리뷰 API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/perfacto/api/reviews")
@RequiredArgsConstructor
public class PerfactoReviewController {

    private final PerfactoReviewService reviewService;

    /**
     * 리뷰 작성
     * POST /perfacto/api/reviews
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
        @Valid @RequestBody ReviewCreateRequest request,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        ReviewResponse response = reviewService.createReview(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "리뷰가 작성되었습니다."));
    }

    /**
     * 리뷰 수정
     * PUT /perfacto/api/reviews/{reviewId}
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
        @PathVariable Long reviewId,
        @RequestParam(required = false) Double rating,
        @RequestParam(required = false) String content,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        ReviewResponse response = reviewService.updateReview(reviewId, rating, content, userId);
        return ResponseEntity.ok(ApiResponse.success(response, "리뷰가 수정되었습니다."));
    }

    /**
     * 리뷰 삭제
     * DELETE /perfacto/api/reviews/{reviewId}
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
        @PathVariable Long reviewId,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "리뷰가 삭제되었습니다."));
    }

    /**
     * 장소별 리뷰 조회
     * GET /perfacto/api/reviews/place/{placeId}
     */
    @GetMapping("/place/{placeId}")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getReviewsByPlace(
        @PathVariable Long placeId,
        @PageableDefault(size = 20, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ReviewResponse> response = reviewService.getReviewsByPlace(placeId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 내가 작성한 리뷰 조회
     * GET /perfacto/api/reviews/my
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getMyReviews(
        @RequestHeader("Authorization") String token,
        @PageableDefault(size = 20, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        Page<ReviewResponse> response = reviewService.getReviewsByUser(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 도움이 됨 증가
     * POST /perfacto/api/reviews/{reviewId}/helpful
     */
    @PostMapping("/{reviewId}/helpful")
    public ResponseEntity<ApiResponse<Void>> incrementHelpfulCount(@PathVariable Long reviewId) {
        reviewService.incrementHelpfulCount(reviewId);
        return ResponseEntity.ok(ApiResponse.success(null, "도움이 됨을 눌렀습니다."));
    }

    /**
     * 도움이 됨 수 상위 리뷰 조회
     * GET /perfacto/api/reviews/place/{placeId}/top-helpful?limit=5
     */
    @GetMapping("/place/{placeId}/top-helpful")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getTopHelpfulReviews(
        @PathVariable Long placeId,
        @RequestParam(defaultValue = "5") int limit
    ) {
        List<ReviewResponse> response = reviewService.getTopHelpfulReviews(placeId, limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
