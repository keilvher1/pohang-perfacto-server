package org.example.scrd.repo;

import org.example.scrd.domain.PerfactoReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Perfacto 리뷰 Repository
 */
@Repository
public interface PerfactoReviewRepository extends JpaRepository<PerfactoReview, Long> {

    /**
     * 장소별 리뷰 조회 (최신순, 활성화된 것만)
     */
    Page<PerfactoReview> findByPlaceIdAndIsActiveTrueOrderByRegDateDesc(
        Long placeId, Pageable pageable);

    /**
     * 사용자별 리뷰 조회 (최신순, 활성화된 것만)
     */
    Page<PerfactoReview> findByUserIdAndIsActiveTrueOrderByRegDateDesc(
        Long userId, Pageable pageable);

    /**
     * 장소의 평균 평점 조회
     */
    @Query("SELECT AVG(r.rating) FROM PerfactoReview r WHERE r.place.id = :placeId AND r.isActive = true")
    Optional<Double> findAverageRatingByPlaceId(@Param("placeId") Long placeId);

    /**
     * 장소별 리뷰 개수
     */
    Long countByPlaceIdAndIsActiveTrue(Long placeId);

    /**
     * 사용자별 리뷰 개수
     */
    Long countByUserIdAndIsActiveTrue(Long userId);

    /**
     * 사용자가 특정 장소에 리뷰를 작성했는지 확인
     */
    boolean existsByUserIdAndPlaceIdAndIsActiveTrue(Long userId, Long placeId);

    /**
     * 장소의 평점별 리뷰 조회
     */
    Page<PerfactoReview> findByPlaceIdAndRatingAndIsActiveTrueOrderByRegDateDesc(
        Long placeId, Double rating, Pageable pageable);

    /**
     * 도움이 됨 수 상위 리뷰 조회
     */
    @Query("SELECT r FROM PerfactoReview r WHERE r.place.id = :placeId AND r.isActive = true " +
           "ORDER BY r.helpfulCount DESC, r.regDate DESC")
    List<PerfactoReview> findTopHelpfulReviewsByPlaceId(
        @Param("placeId") Long placeId, Pageable pageable);
}
