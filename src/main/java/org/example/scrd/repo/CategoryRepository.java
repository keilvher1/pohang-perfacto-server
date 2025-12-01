package org.example.scrd.repo;

import org.example.scrd.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 카테고리 Repository
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 카테고리 코드로 조회
     */
    Optional<Category> findByCode(String code);

    /**
     * 활성화된 카테고리 모두 조회 (표시 순서대로)
     */
    List<Category> findByIsActiveTrueOrderByDisplayOrderAsc();

    /**
     * 카테고리 코드 존재 여부 확인
     */
    boolean existsByCode(String code);
}
