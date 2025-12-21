package org.example.scrd.repo;

import org.example.scrd.domain.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 장소 Repository
 */
@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    /**
     * 활성화된 장소만 ID로 조회
     */
    Optional<Place> findByIdAndIsActiveTrue(Long id);

    /**
     * 카테고리별 장소 조회 (활성화된 것만)
     */
    Page<Place> findByCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable);

    /**
     * 구역별 장소 조회 (활성화된 것만)
     */
    Page<Place> findByDistrictAndIsActiveTrue(String district, Pageable pageable);

    /**
     * 장소명으로 검색 (활성화된 것만)
     */
    Page<Place> findByNameContainingAndIsActiveTrue(String keyword, Pageable pageable);

    /**
     * 카테고리와 구역으로 필터링
     */
    Page<Place> findByCategoryIdAndDistrictAndIsActiveTrue(
        Long categoryId, String district, Pageable pageable);

    /**
     * 좋아요 수 상위 장소 조회 (HOT 장소)
     */
    @Query("SELECT p FROM Place p WHERE p.isActive = true ORDER BY p.likeCount DESC")
    List<Place> findTopPlacesByLikeCount(Pageable pageable);

    /**
     * 조회수 상위 장소 조회
     */
    @Query("SELECT p FROM Place p WHERE p.isActive = true ORDER BY p.viewCount DESC")
    List<Place> findTopPlacesByViewCount(Pageable pageable);

    /**
     * 최신 등록 장소 조회
     */
    @Query("SELECT p FROM Place p WHERE p.isActive = true ORDER BY p.regDate DESC")
    List<Place> findRecentPlaces(Pageable pageable);

    /**
     * 평점 상위 장소 조회
     */
    @Query("SELECT p FROM Place p " +
           "LEFT JOIN p.reviews r " +
           "WHERE p.isActive = true " +
           "GROUP BY p.id " +
           "HAVING COUNT(r) > 0 " +
           "ORDER BY AVG(CASE r.overallRating WHEN 'GOOD' THEN 5 WHEN 'NEUTRAL' THEN 3 WHEN 'BAD' THEN 1 END) DESC")
    List<Place> findTopPlacesByRating(Pageable pageable);

    /**
     * 위치 기반 반경 검색 (Haversine formula)
     * @param latitude 중심 위도
     * @param longitude 중심 경도
     * @param radiusKm 반경 (km)
     */
    @Query(value = "SELECT p.* FROM places p " +
                   "WHERE p.is_active = true " +
                   "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
                   "cos(radians(p.longitude) - radians(:longitude)) + " +
                   "sin(radians(:latitude)) * sin(radians(p.latitude)))) <= :radiusKm " +
                   "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
                   "cos(radians(p.longitude) - radians(:longitude)) + " +
                   "sin(radians(:latitude)) * sin(radians(p.latitude))))",
           nativeQuery = true)
    List<Place> findPlacesWithinRadius(
        @Param("latitude") Double latitude,
        @Param("longitude") Double longitude,
        @Param("radiusKm") Double radiusKm
    );

    /**
     * 특정 사용자가 등록한 장소 조회
     */
    Page<Place> findByCreatedById(Long userId, Pageable pageable);

    /**
     * 카테고리별 장소 개수
     */
    @Query("SELECT COUNT(p) FROM Place p WHERE p.category.id = :categoryId AND p.isActive = true")
    Long countByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 구역별 장소 개수
     */
    @Query("SELECT COUNT(p) FROM Place p WHERE p.district = :district AND p.isActive = true")
    Long countByDistrict(@Param("district") String district);
}
