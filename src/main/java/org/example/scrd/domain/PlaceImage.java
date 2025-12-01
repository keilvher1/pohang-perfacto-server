package org.example.scrd.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.scrd.BaseEntity;

/**
 * 장소 이미지 엔티티
 * 각 장소의 여러 이미지를 저장
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "place_images", indexes = {
    @Index(name = "idx_place_id", columnList = "place_id")
})
public class PlaceImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place; // 연관된 장소

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageUrl; // 이미지 URL (S3 또는 로컬 경로)

    @Column(length = 500)
    private String originalFileName; // 원본 파일명

    @Column(length = 500)
    private String storedFileName; // 저장된 파일명

    @Column
    private Long fileSize; // 파일 크기 (bytes)

    @Builder.Default
    @Column(nullable = false)
    private Integer displayOrder = 0; // 표시 순서 (0이 대표 이미지)

    @Builder.Default
    @Column(nullable = false)
    private Boolean isThumbnail = false; // 썸네일 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy; // 업로드한 사용자

    /**
     * 대표 이미지 여부 확인
     */
    public boolean isMainImage() {
        return this.displayOrder == 0;
    }
}
