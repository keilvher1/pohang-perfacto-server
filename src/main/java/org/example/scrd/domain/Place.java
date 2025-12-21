package org.example.scrd.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.scrd.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 장소 엔티티
 * 포항 지역의 맛집, 숙박, 카페, 관광지 등의 정보를 저장
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "places", indexes = {
    @Index(name = "idx_category", columnList = "category"),
    @Index(name = "idx_district", columnList = "district"),
    @Index(name = "idx_created_by", columnList = "created_by")
})
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name; // 장소명

    @Column(columnDefinition = "TEXT")
    private String description; // 장소 설명

    @Column(nullable = false, length = 100)
    private String district; // 구역 (남구, 북구, 동구 등)

    @Column(nullable = false, length = 500)
    private String address; // 상세 주소

    @Column(nullable = false)
    private Double latitude; // 위도

    @Column(nullable = false)
    private Double longitude; // 경도

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // 카테고리 (음식점, 숙박, 카페, 관광지)

    @Column(length = 20)
    private String phoneNumber; // 전화번호

    @Column(length = 500)
    private String website; // 웹사이트

    @Column(length = 100)
    private String businessHours; // 영업시간

    @Builder.Default
    @Column(nullable = false)
    private Integer likeCount = 0; // 좋아요 수

    @Builder.Default
    @Column(nullable = false)
    private Integer bookmarkCount = 0; // 북마크 수

    @Builder.Default
    @Column(nullable = false)
    private Integer viewCount = 0; // 조회수

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true; // 활성화 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy; // 등록한 사용자

    // 연관 관계
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PlaceImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PerfactoReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Bookmark> bookmarks = new ArrayList<>();

    // 비즈니스 로직
    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void incrementBookmarkCount() {
        this.bookmarkCount++;
    }

    public void decrementBookmarkCount() {
        if (this.bookmarkCount > 0) {
            this.bookmarkCount--;
        }
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void addImage(PlaceImage image) {
        this.images.add(image);
        image.setPlace(this);
    }

    public void removeImage(PlaceImage image) {
        this.images.remove(image);
        image.setPlace(null);
    }

    /**
     * 평균 평점 계산
     */
    public Double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
            .filter(r -> r.getIsActive())
            .mapToDouble(r -> r.getRating())
            .average()
            .orElse(0.0);
    }

    /**
     * 리뷰 개수
     */
    public int getReviewCount() {
        return reviews != null ? reviews.size() : 0;
    }
}
