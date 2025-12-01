package org.example.scrd.dto.response;

import lombok.*;
import org.example.scrd.domain.User;

/**
 * 사용자 간단 응답 DTO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleResponse {

    private Long id;
    private String nickName;
    private String profileImageUrl;
    private Integer point;

    /**
     * Entity를 Response DTO로 변환
     */
    public static UserSimpleResponse from(User user) {
        return UserSimpleResponse.builder()
            .id(user.getId())
            .nickName(user.getNickName())
            .profileImageUrl(user.getProfileImageUrl())
            .point(user.getPoint())
            .build();
    }
}
