package org.example.scrd.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.scrd.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 카테고리 엔티티
 * 장소의 분류 (음식점, 숙박, 카페, 관광지 등)
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code; // 카테고리 코드 (restaurant, accommodation, cafe, attraction)

    @Column(nullable = false, length = 100)
    private String name; // 카테고리 이름 (한글)

    @Column(length = 100)
    private String nameEn; // 카테고리 이름 (영문)

    @Column(columnDefinition = "TEXT")
    private String description; // 카테고리 설명

    @Column(length = 500)
    private String iconUrl; // 카테고리 아이콘 URL

    @Builder.Default
    @Column(nullable = false)
    private Integer displayOrder = 0; // 표시 순서

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true; // 활성화 여부

    // 연관 관계
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Place> places = new ArrayList<>();

    /**
     * 기본 카테고리 생성 헬퍼 메서드
     */
    public static Category createDefault(String code, String name, String nameEn, int displayOrder) {
        return Category.builder()
            .code(code)
            .name(name)
            .nameEn(nameEn)
            .displayOrder(displayOrder)
            .isActive(true)
            .build();
    }
}
