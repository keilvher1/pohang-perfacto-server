package org.example.scrd.controller.response;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class KakaoLoginResponse {
    private String name;
    private String profileImageUrl;
    private String email;
    private String accessToken;
    private String refreshToken;
}