package org.example.scrd.repo;

import org.example.scrd.domain.Place;
import org.example.scrd.domain.SavedPlace;
import org.example.scrd.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedPlaceRepository extends JpaRepository<SavedPlace, Long> {

    /**
     * 저장된 장소 존재 확인
     */
    boolean existsByUserAndPlace(User user, Place place);

    /**
     * 특정 사용자의 저장된 장소 조회
     */
    Optional<SavedPlace> findByUserAndPlace(User user, Place place);

    /**
     * 사용자의 모든 저장된 장소 조회
     */
    List<SavedPlace> findAllByUserOrderByRegDateDesc(User user);

    /**
     * 장소를 저장한 사용자 수 카운트
     */
    long countByPlace(Place place);

    /**
     * 저장된 장소 삭제
     */
    void deleteByUserAndPlace(User user, Place place);
}
