package org.example.scrd.repo;

import org.example.scrd.domain.PlaceImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 장소 이미지 Repository
 */
@Repository
public interface PlaceImageRepository extends JpaRepository<PlaceImage, Long> {

    /**
     * 장소별 이미지 조회 (표시 순서대로)
     */
    List<PlaceImage> findByPlaceIdOrderByDisplayOrderAsc(Long placeId);

    /**
     * 장소의 대표 이미지 조회
     */
    @Query("SELECT pi FROM PlaceImage pi WHERE pi.place.id = :placeId AND pi.displayOrder = 0")
    Optional<PlaceImage> findMainImageByPlaceId(@Param("placeId") Long placeId);

    /**
     * 장소별 이미지 개수
     */
    Long countByPlaceId(Long placeId);

    /**
     * 장소의 모든 이미지 삭제
     */
    void deleteByPlaceId(Long placeId);
}
