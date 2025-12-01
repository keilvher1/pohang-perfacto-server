package org.example.scrd.service;

import org.example.scrd.dto.UserDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverService {

    @Value("${naver.api.key.client}") // application properties에서 네이버 클라이언트 ID 주입
    private String clientId;

    @Value("${naver.api.key.secret}") // application properties에서 네이버 클라이언트 시크릿 주입
    private String clientSecret;

    public UserDto naverLogin(String code, String state, String redirectUri) {
        String accessToken = getAccessToken(code, state, redirectUri); // 응답 받은 code로부터 accessToken 받아내기
        return getNaverUserInfo(accessToken); // accessToken으로부터 사용자 정보 알아내기
    }

    // 네이버 OAuth 서버에서 액세스 토큰을 받아오는 메서드
    private String getAccessToken(String code, String state, String redirectUri) {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성 (네이버 API에 전달할 파라미터 설정)
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code"); // 인증 코드 기반의 토큰 요청
        body.add("client_id", clientId); // 네이버 개발자 센터에서 발급받은 클라이언트 ID
        body.add("client_secret", clientSecret); // 네이버 개발자 센터에서 발급받은 클라이언트 시크릿
        body.add("code", code); // 네이버 로그인 후 받은 인증 코드
        body.add("state", state); // CSRF 방지용 state 값

        // HTTP 요청 생성
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(body, headers);

        // 네이버 서버로 HTTP 요청을 보내고 액세스 토큰을 받아옴
        RestTemplate rt = new RestTemplate();
        try {
            ResponseEntity<String> response = rt.exchange(
                    "https://nid.naver.com/oauth2.0/token",
                    HttpMethod.POST,
                    naverTokenRequest,
                    String.class
            );

            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();

        } catch (Exception e) {
            System.err.println("🔥 [네이버 토큰 발급 실패] message: " + e.getMessage());
            if (e instanceof org.springframework.web.client.HttpClientErrorException httpError) {
                System.err.println("🔥 [네이버 응답 바디] " + httpError.getResponseBodyAsString());
            }
            throw new RuntimeException("네이버 토큰 발급 실패", e);
        }
    }

    // 액세스 토큰을 사용하여 네이버 사용자 정보를 가져오는 메서드
    private UserDto getNaverUserInfo(String accessToken) {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 생성
        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);

        // 네이버 API 서버로 사용자 정보 요청
        RestTemplate rt = new RestTemplate();
        try {
            ResponseEntity<String> response = rt.exchange(
                    "https://openapi.naver.com/v1/nid/me",
                    HttpMethod.GET,
                    naverUserInfoRequest,
                    String.class
            );

            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // 네이버 응답 구조: response.id, response.email, response.name, response.profile_image
            JsonNode responseNode = jsonNode.get("response");
            String id = responseNode.get("id").asText();
            String email = responseNode.has("email") ? responseNode.get("email").asText() : null;
            String name = responseNode.has("name") ? responseNode.get("name").asText() : "네이버 사용자";
            String profileImageUrl = responseNode.has("profile_image") ? responseNode.get("profile_image").asText() : null;

            return UserDto.builder()
                    .naverId(id)
                    .name(name)
                    .email(email)
                    .profileImageUrl(profileImageUrl)
                    .build();

        } catch (Exception e) {
            System.err.println("🔥 [네이버 사용자 정보 요청 실패] message: " + e.getMessage());
            if (e instanceof org.springframework.web.client.HttpClientErrorException httpError) {
                System.err.println("🔥 [네이버 응답 바디] " + httpError.getResponseBodyAsString());
            }
            throw new RuntimeException("네이버 사용자 정보 요청 실패", e);
        }
    }
}
