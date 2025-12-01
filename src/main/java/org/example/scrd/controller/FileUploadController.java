package org.example.scrd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.controller.response.ApiResponse;
import org.example.scrd.service.FileUploadService;
import org.example.scrd.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드 API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/perfacto/api/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    /**
     * 장소 이미지 업로드
     * POST /perfacto/api/upload/place/{placeId}
     */
    @PostMapping("/place/{placeId}")
    public ResponseEntity<ApiResponse<String>> uploadPlaceImage(
        @PathVariable Long placeId,
        @RequestParam("file") MultipartFile file,
        @RequestParam(required = false, defaultValue = "999") Integer displayOrder,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        String imageUrl = fileUploadService.uploadPlaceImage(placeId, file, userId, displayOrder);
        return ResponseEntity.ok(ApiResponse.success(imageUrl, "이미지가 업로드되었습니다."));
    }

    /**
     * 프로필 이미지 업로드
     * POST /perfacto/api/upload/profile
     */
    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<String>> uploadProfileImage(
        @RequestParam("file") MultipartFile file,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        String imageUrl = fileUploadService.uploadProfileImage(file, userId);
        return ResponseEntity.ok(ApiResponse.success(imageUrl, "프로필 이미지가 업로드되었습니다."));
    }

    /**
     * 이미지 삭제
     * DELETE /perfacto/api/upload/{imageId}
     */
    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse<Void>> deletePlaceImage(
        @PathVariable Long imageId,
        @RequestHeader("Authorization") String token
    ) {
        Long userId = JwtUtil.getUserId(
            token.replace("Bearer ", ""),
            System.getenv("JWT_SECRET")
        );

        fileUploadService.deletePlaceImage(imageId, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "이미지가 삭제되었습니다."));
    }
}
