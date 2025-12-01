package org.example.scrd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.controller.response.ApiResponse;
import org.example.scrd.dto.response.PlaceResponse;
import org.example.scrd.service.BookmarkService;
import org.example.scrd.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 북마크 API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/perfacto/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    /**
     * 북마크 추가
     * POST /perfacto/api/bookmarks/{placeId}
     */
    @PostMapping("/{placeId}")
    public ResponseEntity<ApiResponse<Void>> addBookmark(
        @PathVariable Long placeId,
        @RequestParam(required = false) String memo,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        bookmarkService.addBookmark(placeId, userId, memo);
        return ResponseEntity.ok(ApiResponse.success(null, "북마크에 추가했습니다."));
    }

    /**
     * 북마크 제거
     * DELETE /perfacto/api/bookmarks/{placeId}
     */
    @DeleteMapping("/{placeId}")
    public ResponseEntity<ApiResponse<Void>> removeBookmark(
        @PathVariable Long placeId,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        bookmarkService.removeBookmark(placeId, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "북마크에서 제거했습니다."));
    }

    /**
     * 북마크 메모 업데이트
     * PUT /perfacto/api/bookmarks/{placeId}/memo
     */
    @PutMapping("/{placeId}/memo")
    public ResponseEntity<ApiResponse<Void>> updateBookmarkMemo(
        @PathVariable Long placeId,
        @RequestParam String memo,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        bookmarkService.updateBookmarkMemo(placeId, userId, memo);
        return ResponseEntity.ok(ApiResponse.success(null, "메모가 업데이트되었습니다."));
    }

    /**
     * 북마크 여부 확인
     * GET /perfacto/api/bookmarks/{placeId}/check
     */
    @GetMapping("/{placeId}/check")
    public ResponseEntity<ApiResponse<Boolean>> isBookmarked(
        @PathVariable Long placeId,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        boolean isBookmarked = bookmarkService.isBookmarked(placeId, userId);
        return ResponseEntity.ok(ApiResponse.success(isBookmarked));
    }

    /**
     * 사용자가 북마크한 장소 목록
     * GET /perfacto/api/bookmarks/my
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<PlaceResponse>>> getMyBookmarkedPlaces(
        @RequestHeader("Authorization") String token,
        @PageableDefault(size = 20, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        Page<PlaceResponse> response = bookmarkService.getBookmarkedPlaces(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
