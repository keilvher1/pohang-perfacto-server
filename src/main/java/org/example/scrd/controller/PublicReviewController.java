package org.example.scrd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.controller.response.ApiResponse;
import org.example.scrd.dto.response.ReviewResponse;
import org.example.scrd.service.PerfactoReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 리뷰 Public API 컨트롤러 (인증 불필요)
 */
@Slf4j
@RestController
@RequestMapping("/perfacto/every/reviews")
@RequiredArgsConstructor
public class PublicReviewController {

    private final PerfactoReviewService reviewService;

    /**
     * 사용자별 리뷰 조회 (Public)
     * GET /perfacto/every/reviews/user/{userId}?page=0&size=20
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getUserReviews(
        @PathVariable Long userId,
        @PageableDefault(size = 20, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.info("Public API - 사용자별 리뷰 조회: userId={}, page={}, size={}",
            userId, pageable.getPageNumber(), pageable.getPageSize());

        Page<ReviewResponse> response = reviewService.getReviewsByUser(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
