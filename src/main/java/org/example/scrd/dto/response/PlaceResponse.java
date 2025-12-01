package org.example.scrd.dto.response;

import lombok.*;
import org.example.scrd.domain.Place;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 장소 응답 DTO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceResponse {

    private Long id;
    private String name;
    private String description;
    private String district;
    private String address;
    private Double latitude;
    private Double longitude;
    private CategoryResponse category;
    private String phoneNumber;
    private String website;
    private String businessHours;
    private Integer likeCount;
    private Integer bookmarkCount;
    private Integer viewCount;
    private Boolean isActive;
    private Double averageRating;
    private Integer reviewCount;
    private List<String> imageUrls;
    private UserSimpleResponse createdBy;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    /**
     * Entity를 Response DTO로 변환
     */
    public static PlaceResponse from(Place place) {
        return PlaceResponse.builder()
            .id(place.getId())
            .name(place.getName())
            .description(place.getDescription())
            .district(place.getDistrict())
            .address(place.getAddress())
            .latitude(place.getLatitude())
            .longitude(place.getLongitude())
            .category(CategoryResponse.from(place.getCategory()))
            .phoneNumber(place.getPhoneNumber())
            .website(place.getWebsite())
            .businessHours(place.getBusinessHours())
            .likeCount(place.getLikeCount())
            .bookmarkCount(place.getBookmarkCount())
            .viewCount(place.getViewCount())
            .isActive(place.getIsActive())
            .averageRating(place.getAverageRating())
            .reviewCount(place.getReviewCount())
            .imageUrls(place.getImages().stream()
                .map(img -> img.getImageUrl())
                .collect(Collectors.toList()))
            .createdBy(place.getCreatedBy() != null ? UserSimpleResponse.from(place.getCreatedBy()) : null)
            .regDate(place.getRegDate())
            .modDate(place.getModDate())
            .build();
    }

    /**
     * 간단한 버전 (리스트용)
     */
    public static PlaceResponse fromSimple(Place place) {
        String mainImageUrl = place.getImages().stream()
            .filter(img -> img.getDisplayOrder() == 0)
            .findFirst()
            .map(img -> img.getImageUrl())
            .orElse(null);

        return PlaceResponse.builder()
            .id(place.getId())
            .name(place.getName())
            .description(place.getDescription())
            .district(place.getDistrict())
            .address(place.getAddress())
            .latitude(place.getLatitude())
            .longitude(place.getLongitude())
            .category(CategoryResponse.from(place.getCategory()))
            .likeCount(place.getLikeCount())
            .bookmarkCount(place.getBookmarkCount())
            .viewCount(place.getViewCount())
            .averageRating(place.getAverageRating())
            .reviewCount(place.getReviewCount())
            .imageUrls(mainImageUrl != null ? List.of(mainImageUrl) : List.of())
            .regDate(place.getRegDate())
            .build();
    }
}
