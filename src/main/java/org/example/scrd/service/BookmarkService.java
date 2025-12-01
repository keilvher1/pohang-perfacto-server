package org.example.scrd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.domain.Bookmark;
import org.example.scrd.domain.Place;
import org.example.scrd.domain.User;
import org.example.scrd.dto.response.PlaceResponse;
import org.example.scrd.exception.AlreadyJoinedException;
import org.example.scrd.exception.NotFoundException;
import org.example.scrd.repo.BookmarkRepository;
import org.example.scrd.repo.PlaceRepository;
import org.example.scrd.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 북마크 관리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    /**
     * 북마크 추가
     */
    @Transactional
    public void addBookmark(Long placeId, Long userId, String memo) {
        log.info("User {} adding bookmark to place {}", userId, placeId);

        // 이미 북마크했는지 확인
        if (bookmarkRepository.existsByUserIdAndPlaceId(userId, placeId)) {
            throw new AlreadyJoinedException("이미 북마크한 장소입니다.");
        }

        Place place = placeRepository.findByIdAndIsActiveTrue(placeId)
            .orElseThrow(() -> new NotFoundException("장소를 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Bookmark bookmark = Bookmark.create(user, place, memo);
        bookmarkRepository.save(bookmark);

        log.info("Bookmark added successfully");
    }

    /**
     * 북마크 제거
     */
    @Transactional
    public void removeBookmark(Long placeId, Long userId) {
        log.info("User {} removing bookmark from place {}", userId, placeId);

        Bookmark bookmark = bookmarkRepository.findByUserIdAndPlaceId(userId, placeId)
            .orElseThrow(() -> new NotFoundException("북마크를 찾을 수 없습니다."));

        bookmarkRepository.delete(bookmark);

        log.info("Bookmark removed successfully");
    }

    /**
     * 북마크 메모 업데이트
     */
    @Transactional
    public void updateBookmarkMemo(Long placeId, Long userId, String memo) {
        log.info("User {} updating bookmark memo for place {}", userId, placeId);

        Bookmark bookmark = bookmarkRepository.findByUserIdAndPlaceId(userId, placeId)
            .orElseThrow(() -> new NotFoundException("북마크를 찾을 수 없습니다."));

        bookmark.setMemo(memo);
        bookmarkRepository.save(bookmark);

        log.info("Bookmark memo updated successfully");
    }

    /**
     * 북마크 여부 확인
     */
    public boolean isBookmarked(Long placeId, Long userId) {
        return bookmarkRepository.existsByUserIdAndPlaceId(userId, placeId);
    }

    /**
     * 사용자가 북마크한 장소 목록
     */
    public Page<PlaceResponse> getBookmarkedPlaces(Long userId, Pageable pageable) {
        Page<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId, pageable);
        return bookmarks.map(bookmark -> PlaceResponse.fromSimple(bookmark.getPlace()));
    }

    /**
     * 장소의 북마크 개수
     */
    public Long getBookmarkCount(Long placeId) {
        return bookmarkRepository.countByPlaceId(placeId);
    }
}
