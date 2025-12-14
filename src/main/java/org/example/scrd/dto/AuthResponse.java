package org.example.scrd.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private Long userId;
    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken;
}
