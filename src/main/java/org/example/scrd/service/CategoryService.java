package org.example.scrd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.domain.Category;
import org.example.scrd.dto.response.CategoryResponse;
import org.example.scrd.exception.NotFoundException;
import org.example.scrd.repo.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 카테고리 관리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 모든 활성 카테고리 조회
     */
    public List<CategoryResponse> getAllActiveCategories() {
        List<Category> categories = categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
        return categories.stream()
            .map(CategoryResponse::from)
            .collect(Collectors.toList());
    }

    /**
     * 카테고리 ID로 조회
     */
    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다."));
        return CategoryResponse.from(category);
    }

    /**
     * 카테고리 코드로 조회
     */
    public CategoryResponse getCategoryByCode(String code) {
        Category category = categoryRepository.findByCode(code)
            .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다."));
        return CategoryResponse.from(category);
    }

    /**
     * 기본 카테고리 초기화 (앱 시작 시 실행)
     */
    @Transactional
    public void initializeDefaultCategories() {
        log.info("Initializing default categories...");

        if (categoryRepository.count() > 0) {
            log.info("Categories already exist. Skipping initialization.");
            return;
        }

        List<Category> defaultCategories = List.of(
            Category.createDefault("restaurant", "음식점", "Restaurant", 1),
            Category.createDefault("accommodation", "숙박", "Accommodation", 2),
            Category.createDefault("cafe", "카페", "Cafe", 3),
            Category.createDefault("attraction", "관광지", "Attraction", 4)
        );

        categoryRepository.saveAll(defaultCategories);
        log.info("Default categories initialized successfully");
    }
}
