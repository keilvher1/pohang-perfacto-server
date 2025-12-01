package org.example.scrd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.controller.response.ApiResponse;
import org.example.scrd.dto.response.CategoryResponse;
import org.example.scrd.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 카테고리 API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/perfacto/every/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 모든 활성 카테고리 조회
     * GET /perfacto/every/categories
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllActiveCategories();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 카테고리 ID로 조회
     * GET /perfacto/every/categories/{categoryId}
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long categoryId) {
        CategoryResponse response = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 카테고리 코드로 조회
     * GET /perfacto/every/categories/code/{code}
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryByCode(@PathVariable String code) {
        CategoryResponse response = categoryService.getCategoryByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
