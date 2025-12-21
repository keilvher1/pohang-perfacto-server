package org.example.scrd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.domain.*;
import org.example.scrd.dto.request.ReviewCreateRequest;
import org.example.scrd.dto.response.ReviewResponse;
import org.example.scrd.exception.AlreadyJoinedException;
import org.example.scrd.exception.NotFoundException;
import org.example.scrd.exception.UnauthorizedAccessException;
import org.example.scrd.repo.PerfactoReviewRepository;
import org.example.scrd.repo.PlaceRepository;
import org.example.scrd.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 리뷰 관리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerfactoReviewService {

    private final PerfactoReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    /**
     * 리뷰 작성
     */
    @Transactional
    public ReviewResponse createReview(ReviewCreateRequest request, Long userId) {
        log.info("User {} creating review for place {}", userId, request.getPlaceId());

        // 이미 리뷰를 작성했는지 확인
        if (reviewRepository.existsByUserIdAndPlaceIdAndIsActiveTrue(userId, request.getPlaceId())) {
            throw new AlreadyJoinedException("이미 리뷰를 작성한 장소입니다.");
        }

        Place place = placeRepository.findByIdAndIsActiveTrue(request.getPlaceId())
            .orElseThrow(() -> new NotFoundException("장소를 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        // 임시: 기존 rating을 ReviewRating으로 변환
        ReviewRating rating = request.getRating() >= 4 ? ReviewRating.GOOD :
                              request.getRating() >= 2.5 ? ReviewRating.NEUTRAL :
                              ReviewRating.BAD;

        PerfactoReview review = PerfactoReview.create(
            place, user, rating, new HashSet<>(), null, null);

        PerfactoReview savedReview = reviewRepository.save(review);

        // 사용자에게 포인트 지급 (리뷰 작성 보상)
        user.setPoint(user.getPoint() + 500);
        user.setCount(user.getCount() + 1);
        userRepository.save(user);

        log.info("Review created successfully with ID: {}", savedReview.getId());
        return ReviewResponse.from(savedReview);
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public ReviewResponse updateReview(Long reviewId, Double rating, String content, Long userId) {
        log.info("User {} updating review {}", userId, reviewId);

        PerfactoReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new NotFoundException("리뷰를 찾을 수 없습니다."));

        // 작성자만 수정 가능
        if (!review.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("리뷰를 수정할 권한이 없습니다.");
        }

        // 임시: 새 구조에서는 수정 기능 보류
        // if (rating != null) {
        //     review.setOverallRating(...);
        // }

        PerfactoReview updatedReview = reviewRepository.save(review);
        log.info("Review updated successfully");

        return ReviewResponse.from(updatedReview);
    }

    /**
     * 리뷰 삭제 (소프트 삭제)
     */
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        log.info("User {} deleting review {}", userId, reviewId);

        PerfactoReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new NotFoundException("리뷰를 찾을 수 없습니다."));

        // 작성자만 삭제 가능
        if (!review.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("리뷰를 삭제할 권한이 없습니다.");
        }

        review.setIsActive(false);
        reviewRepository.save(review);

        log.info("Review soft deleted");
    }

    /**
     * 장소별 리뷰 조회
     */
    public Page<ReviewResponse> getReviewsByPlace(Long placeId, Pageable pageable) {
        Page<PerfactoReview> reviews = reviewRepository
            .findByPlaceIdAndIsActiveTrueOrderByRegDateDesc(placeId, pageable);
        return reviews.map(ReviewResponse::from);
    }

    /**
     * 사용자별 리뷰 조회
     */
    public Page<ReviewResponse> getReviewsByUser(Long userId, Pageable pageable) {
        Page<PerfactoReview> reviews = reviewRepository
            .findByUserIdAndIsActiveTrueOrderByRegDateDesc(userId, pageable);
        return reviews.map(ReviewResponse::from);
    }

    /**
     * 도움이 됨 증가
     */
    @Transactional
    public void incrementHelpfulCount(Long reviewId) {
        PerfactoReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new NotFoundException("리뷰를 찾을 수 없습니다."));

        review.incrementHelpfulCount();
        reviewRepository.save(review);
    }

    /**
     * 장소의 평균 평점 조회
     */
    public Double getAverageRating(Long placeId) {
        return reviewRepository.calculateAverageRating(placeId);
    }

    /**
     * 좋아요 수 상위 리뷰 조회
     */
    public List<ReviewResponse> getTopHelpfulReviews(Long placeId, int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        List<PerfactoReview> reviews = reviewRepository
            .findTopLikedReviewsByPlaceId(placeId, pageable);
        return reviews.stream()
            .map(ReviewResponse::from)
            .collect(Collectors.toList());
    }
}
