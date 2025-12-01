package org.example.scrd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.domain.Like;
import org.example.scrd.domain.Place;
import org.example.scrd.domain.User;
import org.example.scrd.dto.response.PlaceResponse;
import org.example.scrd.exception.AlreadyJoinedException;
import org.example.scrd.exception.NotFoundException;
import org.example.scrd.repo.LikeRepository;
import org.example.scrd.repo.PlaceRepository;
import org.example.scrd.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 좋아요 관리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    /**
     * 좋아요 추가
     */
    @Transactional
    public void addLike(Long placeId, Long userId) {
        log.info("User {} adding like to place {}", userId, placeId);

        // 이미 좋아요를 눌렀는지 확인
        if (likeRepository.existsByUserIdAndPlaceId(userId, placeId)) {
            throw new AlreadyJoinedException("이미 좋아요를 누른 장소입니다.");
        }

        Place place = placeRepository.findByIdAndIsActiveTrue(placeId)
            .orElseThrow(() -> new NotFoundException("장소를 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Like like = Like.create(user, place);
        likeRepository.save(like);

        log.info("Like added successfully");
    }

    /**
     * 좋아요 취소
     */
    @Transactional
    public void removeLike(Long placeId, Long userId) {
        log.info("User {} removing like from place {}", userId, placeId);

        Like like = likeRepository.findByUserIdAndPlaceId(userId, placeId)
            .orElseThrow(() -> new NotFoundException("좋아요를 찾을 수 없습니다."));

        likeRepository.delete(like);

        log.info("Like removed successfully");
    }

    /**
     * 좋아요 여부 확인
     */
    public boolean isLiked(Long placeId, Long userId) {
        return likeRepository.existsByUserIdAndPlaceId(userId, placeId);
    }

    /**
     * 사용자가 좋아요한 장소 목록
     */
    public Page<PlaceResponse> getLikedPlaces(Long userId, Pageable pageable) {
        Page<Like> likes = likeRepository.findByUserId(userId, pageable);
        return likes.map(like -> PlaceResponse.fromSimple(like.getPlace()));
    }

    /**
     * 장소의 좋아요 개수
     */
    public Long getLikeCount(Long placeId) {
        return likeRepository.countByPlaceId(placeId);
    }
}
