package org.example.scrd.repo;

import org.example.scrd.domain.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 북마크 Repository
 */
@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    /**
     * 사용자가 특정 장소를 북마크했는지 확인
     */
    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    /**
     * 사용자가 북마크한 장소 조회
     */
    @Query("SELECT b FROM Bookmark b JOIN FETCH b.place WHERE b.user.id = :userId")
    Page<Bookmark> findByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * 사용자와 장소로 북마크 조회
     */
    Optional<Bookmark> findByUserIdAndPlaceId(Long userId, Long placeId);

    /**
     * 장소별 북마크 개수
     */
    Long countByPlaceId(Long placeId);

    /**
     * 사용자가 북마크한 장소 개수
     */
    Long countByUserId(Long userId);
}
