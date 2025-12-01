package org.example.scrd.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.controller.response.ApiResponse;
import org.example.scrd.dto.request.PlaceCreateRequest;
import org.example.scrd.dto.request.PlaceUpdateRequest;
import org.example.scrd.dto.response.PlaceResponse;
import org.example.scrd.service.PlaceService;
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
 * 장소 관리 API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/perfacto/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    /**
     * 장소 생성
     * POST /perfacto/api/places
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PlaceResponse>> createPlace(
        @Valid @RequestBody PlaceCreateRequest request,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        PlaceResponse response = placeService.createPlace(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "장소가 생성되었습니다."));
    }

    /**
     * 장소 수정
     * PUT /perfacto/api/places/{placeId}
     */
    @PutMapping("/{placeId}")
    public ResponseEntity<ApiResponse<PlaceResponse>> updatePlace(
        @PathVariable Long placeId,
        @Valid @RequestBody PlaceUpdateRequest request,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        PlaceResponse response = placeService.updatePlace(placeId, request, userId);
        return ResponseEntity.ok(ApiResponse.success(response, "장소가 수정되었습니다."));
    }

    /**
     * 장소 상세 조회
     * GET /perfacto/api/places/{placeId}
     */
    @GetMapping("/{placeId}")
    public ResponseEntity<ApiResponse<PlaceResponse>> getPlace(@PathVariable Long placeId) {
        PlaceResponse response = placeService.getPlace(placeId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 장소 삭제
     * DELETE /perfacto/api/places/{placeId}
     */
    @DeleteMapping("/{placeId}")
    public ResponseEntity<ApiResponse<Void>> deletePlace(
        @PathVariable Long placeId,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        placeService.deletePlace(placeId, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "장소가 삭제되었습니다."));
    }

    /**
     * 모든 장소 조회 (페이징)
     * GET /perfacto/api/places
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PlaceResponse>>> getAllPlaces(
        @PageableDefault(size = 20, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PlaceResponse> response = placeService.getAllPlaces(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 카테고리별 장소 조회
     * GET /perfacto/api/places/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<PlaceResponse>>> getPlacesByCategory(
        @PathVariable Long categoryId,
        @PageableDefault(size = 20, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PlaceResponse> response = placeService.getPlacesByCategory(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 구역별 장소 조회
     * GET /perfacto/api/places/district/{district}
     */
    @GetMapping("/district/{district}")
    public ResponseEntity<ApiResponse<Page<PlaceResponse>>> getPlacesByDistrict(
        @PathVariable String district,
        @PageableDefault(size = 20, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PlaceResponse> response = placeService.getPlacesByDistrict(district, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 장소명으로 검색
     * GET /perfacto/api/places/search?keyword=
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<PlaceResponse>>> searchPlaces(
        @RequestParam String keyword,
        @PageableDefault(size = 20, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PlaceResponse> response = placeService.searchPlaces(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * HOT 장소 조회 (좋아요 순)
     * GET /perfacto/api/places/hot?limit=10
     */
    @GetMapping("/hot")
    public ResponseEntity<ApiResponse<List<PlaceResponse>>> getHotPlaces(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<PlaceResponse> response = placeService.getHotPlaces(limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 최신 장소 조회
     * GET /perfacto/api/places/recent?limit=10
     */
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<PlaceResponse>>> getRecentPlaces(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<PlaceResponse> response = placeService.getRecentPlaces(limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 평점 상위 장소 조회
     * GET /perfacto/api/places/top-rated?limit=10
     */
    @GetMapping("/top-rated")
    public ResponseEntity<ApiResponse<List<PlaceResponse>>> getTopRatedPlaces(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<PlaceResponse> response = placeService.getTopRatedPlaces(limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 위치 기반 주변 장소 검색
     * GET /perfacto/api/places/nearby?lat=36.019&lon=129.343&radius=5
     */
    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<List<PlaceResponse>>> getNearbyPlaces(
        @RequestParam Double lat,
        @RequestParam Double lon,
        @RequestParam(defaultValue = "5.0") Double radius
    ) {
        List<PlaceResponse> response = placeService.getNearbyPlaces(lat, lon, radius);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 사용자가 등록한 장소 조회
     * GET /perfacto/api/places/my
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<PlaceResponse>>> getMyPlaces(
        @RequestHeader("Authorization") String token,
        @PageableDefault(size = 20, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        Page<PlaceResponse> response = placeService.getPlacesByUser(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
