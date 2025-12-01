package org.example.scrd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.domain.*;
import org.example.scrd.dto.request.PlaceCreateRequest;
import org.example.scrd.dto.request.PlaceUpdateRequest;
import org.example.scrd.dto.response.PlaceResponse;
import org.example.scrd.exception.NotFoundException;
import org.example.scrd.exception.UnauthorizedAccessException;
import org.example.scrd.repo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 장소 관리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;
    private final PlaceImageRepository placeImageRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;

    /**
     * 장소 생성
     */
    @Transactional
    public PlaceResponse createPlace(PlaceCreateRequest request, Long userId) {
        log.info("Creating place: {} by user: {}", request.getName(), userId);

        // 카테고리 조회
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다."));

        // 사용자 조회
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        // 장소 생성
        Place place = Place.builder()
            .name(request.getName())
            .description(request.getDescription())
            .district(request.getDistrict())
            .address(request.getAddress())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .category(category)
            .phoneNumber(request.getPhoneNumber())
            .website(request.getWebsite())
            .businessHours(request.getBusinessHours())
            .createdBy(user)
            .isActive(true)
            .likeCount(0)
            .bookmarkCount(0)
            .viewCount(0)
            .build();

        Place savedPlace = placeRepository.save(place);

        // 사용자에게 포인트 지급 (장소 등록 보상)
        user.setPoint(user.getPoint() + 100);
        userRepository.save(user);

        log.info("Place created successfully with ID: {}", savedPlace.getId());
        return PlaceResponse.from(savedPlace);
    }

    /**
     * 장소 수정
     */
    @Transactional
    public PlaceResponse updatePlace(Long placeId, PlaceUpdateRequest request, Long userId) {
        log.info("Updating place ID: {} by user: {}", placeId, userId);

        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> new NotFoundException("장소를 찾을 수 없습니다."));

        // 작성자만 수정 가능
        if (!place.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedAccessException("장소를 수정할 권한이 없습니다.");
        }

        // 필드 업데이트
        if (request.getName() != null) {
            place.setName(request.getName());
        }
        if (request.getDescription() != null) {
            place.setDescription(request.getDescription());
        }
        if (request.getDistrict() != null) {
            place.setDistrict(request.getDistrict());
        }
        if (request.getAddress() != null) {
            place.setAddress(request.getAddress());
        }
        if (request.getLatitude() != null) {
            place.setLatitude(request.getLatitude());
        }
        if (request.getLongitude() != null) {
            place.setLongitude(request.getLongitude());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다."));
            place.setCategory(category);
        }
        if (request.getPhoneNumber() != null) {
            place.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getWebsite() != null) {
            place.setWebsite(request.getWebsite());
        }
        if (request.getBusinessHours() != null) {
            place.setBusinessHours(request.getBusinessHours());
        }
        if (request.getIsActive() != null) {
            place.setIsActive(request.getIsActive());
        }

        Place updatedPlace = placeRepository.save(place);
        log.info("Place updated successfully: {}", placeId);

        return PlaceResponse.from(updatedPlace);
    }

    /**
     * 장소 상세 조회
     */
    @Transactional
    public PlaceResponse getPlace(Long placeId) {
        Place place = placeRepository.findByIdAndIsActiveTrue(placeId)
            .orElseThrow(() -> new NotFoundException("장소를 찾을 수 없습니다."));

        // 조회수 증가
        place.incrementViewCount();
        placeRepository.save(place);

        return PlaceResponse.from(place);
    }

    /**
     * 장소 삭제 (소프트 삭제)
     */
    @Transactional
    public void deletePlace(Long placeId, Long userId) {
        log.info("Deleting place ID: {} by user: {}", placeId, userId);

        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> new NotFoundException("장소를 찾을 수 없습니다."));

        // 작성자만 삭제 가능
        if (!place.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedAccessException("장소를 삭제할 권한이 없습니다.");
        }

        place.setIsActive(false);
        placeRepository.save(place);

        log.info("Place soft deleted: {}", placeId);
    }

    /**
     * 모든 장소 조회 (페이징)
     */
    public Page<PlaceResponse> getAllPlaces(Pageable pageable) {
        Page<Place> places = placeRepository.findAll(pageable);
        return places.map(PlaceResponse::fromSimple);
    }

    /**
     * 카테고리별 장소 조회
     */
    public Page<PlaceResponse> getPlacesByCategory(Long categoryId, Pageable pageable) {
        Page<Place> places = placeRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable);
        return places.map(PlaceResponse::fromSimple);
    }

    /**
     * 구역별 장소 조회
     */
    public Page<PlaceResponse> getPlacesByDistrict(String district, Pageable pageable) {
        Page<Place> places = placeRepository.findByDistrictAndIsActiveTrue(district, pageable);
        return places.map(PlaceResponse::fromSimple);
    }

    /**
     * 장소명으로 검색
     */
    public Page<PlaceResponse> searchPlaces(String keyword, Pageable pageable) {
        Page<Place> places = placeRepository.findByNameContainingAndIsActiveTrue(keyword, pageable);
        return places.map(PlaceResponse::fromSimple);
    }

    /**
     * HOT 장소 조회 (좋아요 순)
     */
    public List<PlaceResponse> getHotPlaces(int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        List<Place> places = placeRepository.findTopPlacesByLikeCount(pageable);
        return places.stream()
            .map(PlaceResponse::fromSimple)
            .collect(Collectors.toList());
    }

    /**
     * 최신 장소 조회
     */
    public List<PlaceResponse> getRecentPlaces(int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        List<Place> places = placeRepository.findRecentPlaces(pageable);
        return places.stream()
            .map(PlaceResponse::fromSimple)
            .collect(Collectors.toList());
    }

    /**
     * 평점 상위 장소 조회
     */
    public List<PlaceResponse> getTopRatedPlaces(int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        List<Place> places = placeRepository.findTopPlacesByRating(pageable);
        return places.stream()
            .map(PlaceResponse::fromSimple)
            .collect(Collectors.toList());
    }

    /**
     * 위치 기반 주변 장소 검색
     */
    public List<PlaceResponse> getNearbyPlaces(Double latitude, Double longitude, Double radiusKm) {
        List<Place> places = placeRepository.findPlacesWithinRadius(latitude, longitude, radiusKm);
        return places.stream()
            .map(PlaceResponse::fromSimple)
            .collect(Collectors.toList());
    }

    /**
     * 사용자가 등록한 장소 조회
     */
    public Page<PlaceResponse> getPlacesByUser(Long userId, Pageable pageable) {
        Page<Place> places = placeRepository.findByCreatedById(userId, pageable);
        return places.map(PlaceResponse::fromSimple);
    }

    /**
     * 카테고리와 구역으로 필터링
     */
    public Page<PlaceResponse> getPlacesByCategoryAndDistrict(
        Long categoryId, String district, Pageable pageable) {
        Page<Place> places = placeRepository.findByCategoryIdAndDistrictAndIsActiveTrue(
            categoryId, district, pageable);
        return places.map(PlaceResponse::fromSimple);
    }
}
