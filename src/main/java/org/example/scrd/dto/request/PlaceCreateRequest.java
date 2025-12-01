package org.example.scrd.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * 장소 생성 요청 DTO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceCreateRequest {

    @NotBlank(message = "장소명은 필수입니다")
    @Size(max = 200, message = "장소명은 200자 이하여야 합니다")
    private String name;

    @Size(max = 2000, message = "설명은 2000자 이하여야 합니다")
    private String description;

    @NotBlank(message = "구역은 필수입니다")
    @Size(max = 100, message = "구역은 100자 이하여야 합니다")
    private String district;

    @NotBlank(message = "주소는 필수입니다")
    @Size(max = 500, message = "주소는 500자 이하여야 합니다")
    private String address;

    @NotNull(message = "위도는 필수입니다")
    @DecimalMin(value = "-90.0", message = "위도는 -90.0 이상이어야 합니다")
    @DecimalMax(value = "90.0", message = "위도는 90.0 이하여야 합니다")
    private Double latitude;

    @NotNull(message = "경도는 필수입니다")
    @DecimalMin(value = "-180.0", message = "경도는 -180.0 이상이어야 합니다")
    @DecimalMax(value = "180.0", message = "경도는 180.0 이하여야 합니다")
    private Double longitude;

    @NotNull(message = "카테고리 ID는 필수입니다")
    private Long categoryId;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다")
    private String phoneNumber;

    @Size(max = 500, message = "웹사이트는 500자 이하여야 합니다")
    private String website;

    @Size(max = 100, message = "영업시간은 100자 이하여야 합니다")
    private String businessHours;
}
