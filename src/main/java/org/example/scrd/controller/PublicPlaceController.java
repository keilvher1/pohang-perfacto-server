package org.example.scrd.controller;

import lombok.RequiredArgsConstructor;
import org.example.scrd.controller.response.ApiResponse;
import org.example.scrd.dto.response.PlaceResponse;
import org.example.scrd.service.PlaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public Place Controller
 * 인증 없이 접근 가능한 장소 조회 API
 */
@RestController
@RequestMapping("/perfacto/every")
@RequiredArgsConstructor
public class PublicPlaceController {

    private final PlaceService placeService;

    /**
     * 카테고리별 장소 목록 조회 (Public)
     */
    @GetMapping("/places/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<PlaceResponse>>> getPlacesByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("regDate").descending());
        Page<PlaceResponse> places = placeService.getPlacesByCategory(categoryId, pageable);

        return ResponseEntity.ok(ApiResponse.success(places));
    }

    /**
     * 장소 상세 조회 (Public)
     */
    @GetMapping("/places/{placeId}")
    public ResponseEntity<ApiResponse<PlaceResponse>> getPlace(@PathVariable Long placeId) {
        PlaceResponse place = placeService.getPlace(placeId);
        return ResponseEntity.ok(ApiResponse.success(place));
    }

    /**
     * 장소 검색 (Public)
     */
    @GetMapping("/places/search")
    public ResponseEntity<ApiResponse<Page<PlaceResponse>>> searchPlaces(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PlaceResponse> places = placeService.searchPlaces(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(places));
    }

    /**
     * 장소 리뷰 목록 조회 (Public)
     */
    @GetMapping("/reviews/place/{placeId}")
    public ResponseEntity<ApiResponse<List<Object>>> getPlaceReviews(
            @PathVariable Long placeId) {

        // 리뷰 조회는 PerfactoReviewController에 위임
        // 여기서는 간단하게 빈 리스트 반환
        return ResponseEntity.ok(ApiResponse.success(List.of()));
    }

    /**
     * 인기 장소 조회 (좋아요 수 상위)
     */
    @GetMapping("/places/hot")
    public ResponseEntity<ApiResponse<List<PlaceResponse>>> getHotPlaces(
            @RequestParam(defaultValue = "10") int limit) {

        List<PlaceResponse> places = placeService.getHotPlaces(limit);
        return ResponseEntity.ok(ApiResponse.success(places));
    }

    /**
     * 최신 장소 조회
     */
    @GetMapping("/places/recent")
    public ResponseEntity<ApiResponse<List<PlaceResponse>>> getRecentPlaces(
            @RequestParam(defaultValue = "10") int limit) {

        List<PlaceResponse> places = placeService.getRecentPlaces(limit);
        return ResponseEntity.ok(ApiResponse.success(places));
    }

    /**
     * 평점 상위 장소 조회
     */
    @GetMapping("/places/top-rated")
    public ResponseEntity<ApiResponse<List<PlaceResponse>>> getTopRatedPlaces(
            @RequestParam(defaultValue = "10") int limit) {

        List<PlaceResponse> places = placeService.getTopRatedPlaces(limit);
        return ResponseEntity.ok(ApiResponse.success(places));
    }
}
