package org.example.scrd.repo;

import org.example.scrd.domain.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 좋아요 Repository
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    /**
     * 사용자가 특정 장소에 좋아요를 눌렀는지 확인
     */
    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    /**
     * 사용자가 좋아요한 장소 조회
     */
    @Query("SELECT l FROM Like l JOIN FETCH l.place WHERE l.user.id = :userId")
    Page<Like> findByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * 사용자와 장소로 좋아요 조회
     */
    Optional<Like> findByUserIdAndPlaceId(Long userId, Long placeId);

    /**
     * 장소별 좋아요 개수
     */
    Long countByPlaceId(Long placeId);

    /**
     * 사용자가 좋아요한 장소 개수
     */
    Long countByUserId(Long userId);
}
