package org.example.scrd.dto.response;

import lombok.*;
import org.example.scrd.domain.Category;

/**
 * 카테고리 응답 DTO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Long id;
    private String code;
    private String name;
    private String nameEn;
    private String description;
    private String iconUrl;
    private Integer displayOrder;
    private Boolean isActive;

    /**
     * Entity를 Response DTO로 변환
     */
    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
            .id(category.getId())
            .code(category.getCode())
            .name(category.getName())
            .nameEn(category.getNameEn())
            .description(category.getDescription())
            .iconUrl(category.getIconUrl())
            .displayOrder(category.getDisplayOrder())
            .isActive(category.getIsActive())
            .build();
    }
}
