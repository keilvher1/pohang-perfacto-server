package org.example.scrd.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 애플리케이션 시작 시 초기 데이터 설정
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final CategoryService categoryService;

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            log.info("Initializing application data...");

            // 기본 카테고리 초기화
            categoryService.initializeDefaultCategories();

            log.info("Application data initialization completed.");
        };
    }
}
