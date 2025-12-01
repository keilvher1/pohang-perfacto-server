package org.example.scrd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.controller.response.ApiResponse;
import org.example.scrd.dto.response.PlaceResponse;
import org.example.scrd.service.LikeService;
import org.example.scrd.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 좋아요 API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/perfacto/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /**
     * 좋아요 추가
     * POST /perfacto/api/likes/{placeId}
     */
    @PostMapping("/{placeId}")
    public ResponseEntity<ApiResponse<Void>> addLike(
        @PathVariable Long placeId,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        likeService.addLike(placeId, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "좋아요를 눌렀습니다."));
    }

    /**
     * 좋아요 취소
     * DELETE /perfacto/api/likes/{placeId}
     */
    @DeleteMapping("/{placeId}")
    public ResponseEntity<ApiResponse<Void>> removeLike(
        @PathVariable Long placeId,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        likeService.removeLike(placeId, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "좋아요를 취소했습니다."));
    }

    /**
     * 좋아요 여부 확인
     * GET /perfacto/api/likes/{placeId}/check
     */
    @GetMapping("/{placeId}/check")
    public ResponseEntity<ApiResponse<Boolean>> isLiked(
        @PathVariable Long placeId,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        boolean isLiked = likeService.isLiked(placeId, userId);
        return ResponseEntity.ok(ApiResponse.success(isLiked));
    }

    /**
     * 사용자가 좋아요한 장소 목록
     * GET /perfacto/api/likes/my
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<PlaceResponse>>> getMyLikedPlaces(
        @RequestHeader("Authorization") String token,
        @PageableDefault(size = 20, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        Page<PlaceResponse> response = likeService.getLikedPlaces(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
