package org.example.scrd.repo;

import org.example.scrd.domain.PerfactoReview;
import org.example.scrd.domain.Place;
import org.example.scrd.domain.ReviewRating;
import org.example.scrd.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Perfacto 리뷰 Repository (피벗팅 버전)
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
     * 장소의 모든 리뷰 조회
     */
    List<PerfactoReview> findAllByPlaceAndIsActiveTrue(Place place);

    /**
     * 사용자가 작성한 모든 리뷰 조회
     */
    List<PerfactoReview> findAllByUserOrderByRegDateDesc(User user);

    /**
     * 사용자가 특정 장소에 작성한 리뷰 조회
     */
    Optional<PerfactoReview> findByUserAndPlace(User user, Place place);

    /**
     * 팔로우한 사용자들의 리뷰 조회
     */
    @Query("SELECT r FROM PerfactoReview r WHERE r.place.id = :placeId AND r.user.id IN :followingIds AND r.isActive = true ORDER BY r.regDate DESC")
    List<PerfactoReview> findFollowingReviews(@Param("placeId") Long placeId, @Param("followingIds") List<Long> followingIds);

    /**
     * 특정 카테고리의 사용자 리뷰 중 최고 평점 리뷰 조회
     */
    @Query("SELECT r FROM PerfactoReview r JOIN r.place p WHERE r.user.id = :userId AND p.category.id = :categoryId AND r.overallRating = :rating ORDER BY r.regDate DESC")
    List<PerfactoReview> findTopRatedReviewsByUserAndCategory(
        @Param("userId") Long userId,
        @Param("categoryId") Long categoryId,
        @Param("rating") ReviewRating rating
    );

    /**
     * 장소의 평균 평점 계산 (전체)
     */
    @Query("SELECT AVG(CASE r.overallRating WHEN 'GOOD' THEN 5 WHEN 'NEUTRAL' THEN 3 WHEN 'BAD' THEN 1 END) FROM PerfactoReview r WHERE r.place.id = :placeId AND r.isActive = true")
    Double calculateAverageRating(@Param("placeId") Long placeId);

    /**
     * 장소의 평균 평점 계산 (팔로우한 사용자들만)
     */
    @Query("SELECT AVG(CASE r.overallRating WHEN 'GOOD' THEN 5 WHEN 'NEUTRAL' THEN 3 WHEN 'BAD' THEN 1 END) FROM PerfactoReview r WHERE r.place.id = :placeId AND r.user.id IN :followingIds AND r.isActive = true")
    Double calculateFollowingAverageRating(@Param("placeId") Long placeId, @Param("followingIds") List<Long> followingIds);

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
     * 좋아요 수 상위 리뷰 조회
     */
    @Query("SELECT r FROM PerfactoReview r WHERE r.place.id = :placeId AND r.isActive = true " +
           "ORDER BY r.likeCount DESC, r.regDate DESC")
    List<PerfactoReview> findTopLikedReviewsByPlaceId(
        @Param("placeId") Long placeId, Pageable pageable);
}
