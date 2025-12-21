package org.example.scrd.service;

import lombok.RequiredArgsConstructor;
import org.example.scrd.domain.Place;
import org.example.scrd.domain.SavedPlace;
import org.example.scrd.domain.User;
import org.example.scrd.repo.PlaceRepository;
import org.example.scrd.repo.SavedPlaceRepository;
import org.example.scrd.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 저장된 장소 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SavedPlaceService {

    private final SavedPlaceRepository savedPlaceRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    /**
     * 장소 저장
     */
    @Transactional
    public void savePlace(Long userId, Long placeId, String memo) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> new IllegalArgumentException("장소를 찾을 수 없습니다."));

        // 이미 저장했는지 확인
        if (savedPlaceRepository.existsByUserAndPlace(user, place)) {
            throw new IllegalStateException("이미 저장한 장소입니다.");
        }

        // 저장 생성
        SavedPlace savedPlace = SavedPlace.create(user, place, memo);
        savedPlaceRepository.save(savedPlace);
    }

    /**
     * 장소 저장 취소
     */
    @Transactional
    public void unsavePlace(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> new IllegalArgumentException("장소를 찾을 수 없습니다."));

        // 저장 정보 찾기
        SavedPlace savedPlace = savedPlaceRepository.findByUserAndPlace(user, place)
            .orElseThrow(() -> new IllegalArgumentException("저장 정보가 존재하지 않습니다."));

        // 저장 삭제
        savedPlaceRepository.delete(savedPlace);
    }

    /**
     * 저장된 장소 목록 조회
     */
    public List<Place> getSavedPlaces(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<SavedPlace> savedPlaces = savedPlaceRepository.findAllByUserOrderByRegDateDesc(user);

        return savedPlaces.stream()
            .map(SavedPlace::getPlace)
            .collect(Collectors.toList());
    }

    /**
     * 저장 여부 확인
     */
    public boolean isSaved(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> new IllegalArgumentException("장소를 찾을 수 없습니다."));

        return savedPlaceRepository.existsByUserAndPlace(user, place);
    }
}
