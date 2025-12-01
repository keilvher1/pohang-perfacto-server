package org.example.scrd.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NaverLoginResponse {
    private String name;
    private String email;
    private String profileImageUrl;
    private String naverId;
}
