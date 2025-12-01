# Perfacto Server - 소셜 로그인 구현 가이드

## 📝 개요

Perfacto Server는 **카카오**, **네이버**, **애플** 소셜 로그인을 JWT 토큰 기반 인증으로 구현했습니다.

---

## 🔧 구현된 기능

### 1. 카카오 로그인
- ✅ 카카오 OAuth2.0 인증
- ✅ JWT Access Token 및 Refresh Token 발급
- ✅ 사용자 정보 자동 저장 및 업데이트

### 2. 네이버 로그인
- ✅ 네이버 OAuth2.0 인증
- ✅ JWT Access Token 및 Refresh Token 발급
- ✅ 사용자 정보 자동 저장 및 업데이트

### 3. 애플 로그인
- ✅ Apple Sign In 인증
- ✅ JWT Access Token 및 Refresh Token 발급
- ✅ 사용자 정보 자동 저장 및 업데이트

---

## 📦 의존성

이미 `build.gradle`에 필요한 의존성이 추가되어 있습니다:

```gradle
// JWT
implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.5'

// Apple Sign In
implementation 'com.nimbusds:nimbus-jose-jwt:9.37.3'
implementation 'org.bouncycastle:bcprov-jdk15on:1.70'
implementation 'org.bouncycastle:bcpkix-jdk15on:1.70'

// Spring Security
implementation 'org.springframework.boot:spring-boot-starter-security'
```

---

## ⚙️ 설정 방법

### 1. application.yml 설정

다음 설정 값들을 실제 발급받은 값으로 변경하세요:

```yaml
# 카카오 로그인 설정
kakao:
  api:
    key:
      client: "YOUR_KAKAO_CLIENT_ID"  # 카카오 개발자센터에서 발급
    auth-url: "https://kauth.kakao.com/oauth/authorize"
    redirect-uri: "http://localhost:8080/api/perfacto/auth/kakao-login"

# 네이버 로그인 설정
naver:
  api:
    key:
      client: "YOUR_NAVER_CLIENT_ID"  # 네이버 개발자센터에서 발급
      secret: "YOUR_NAVER_CLIENT_SECRET"  # 네이버 개발자센터에서 발급
    auth-url: "https://nid.naver.com/oauth2.0/authorize"
    redirect-uri: "http://localhost:8080/api/perfacto/auth/naver-login"

# 애플 로그인 설정
apple:
  api:
    key:
      team-id: "YOUR_APPLE_TEAM_ID"  # Apple Developer Team ID
      key-id: "YOUR_APPLE_KEY_ID"  # Apple Sign In Key ID
      client-id: "YOUR_APPLE_CLIENT_ID"  # Apple Service ID
      redirect-url: "http://localhost:8080/api/perfacto/auth/apple-login"
      path: "/keys/AuthKey.p8"  # Apple Private Key 파일 경로

# JWT 설정
custom:
  jwt:
    secret: "YOUR_JWT_SECRET_KEY"  # JWT 시크릿 키
    expire-time-ms: 7200000000  # Access Token 만료 시간 (2시간)
    refresh-expire-time-ms: 7200000000  # Refresh Token 만료 시간 (2시간)
```

---

## 🔑 API 키 발급 방법

### 카카오 로그인

1. [Kakao Developers](https://developers.kakao.com/)에 접속
2. "내 애플리케이션" > "애플리케이션 추가하기"
3. **REST API 키**를 `kakao.api.key.client`에 입력
4. "플랫폼 설정" > "Web" 추가
5. "Redirect URI" 설정: `http://localhost:8080/api/perfacto/auth/kakao-login`
6. "동의 항목" 설정: 닉네임, 프로필 사진, 카카오계정(이메일) 필수 동의

### 네이버 로그인

1. [네이버 개발자센터](https://developers.naver.com/)에 접속
2. "Application" > "애플리케이션 등록"
3. **Client ID**를 `naver.api.key.client`에 입력
4. **Client Secret**을 `naver.api.key.secret`에 입력
5. "사용 API": 네이버 로그인 선택
6. "서비스 URL": `http://localhost:8080`
7. "Callback URL": `http://localhost:8080/api/perfacto/auth/naver-login`
8. "제공 정보": 이메일, 닉네임, 프로필 사진 선택

### 애플 로그인

1. [Apple Developer](https://developer.apple.com/)에 로그인
2. "Certificates, Identifiers & Profiles" > "Identifiers"
3. **App ID** 생성 및 "Sign In with Apple" 활성화
4. **Services ID** 생성:
   - Identifier: `apple.api.key.client-id`에 입력
   - Return URLs: `http://localhost:8080/api/perfacto/auth/apple-login`
5. **Key** 생성:
   - "Sign In with Apple" 활성화
   - Key ID를 `apple.api.key.key-id`에 입력
   - `.p8` 파일 다운로드 후 `src/main/resources/keys/AuthKey.p8`에 저장
6. Team ID는 Apple Developer 계정 페이지에서 확인

---

## 🚀 API 엔드포인트

### 1. 카카오 로그인

```
GET /perfacto/auth/kakao-login?code={AUTHORIZATION_CODE}
```

**요청 헤더:**
```
Origin: http://localhost:3000
```

**응답:**
```json
{
  "name": "홍길동",
  "email": "user@example.com",
  "profileImageUrl": "https://..."
}
```

**응답 헤더:**
```
Authorization: Bearer {ACCESS_TOKEN}
X-Refresh-Token: {REFRESH_TOKEN}
```

### 2. 네이버 로그인

```
GET /perfacto/auth/naver-login?code={AUTHORIZATION_CODE}&state={STATE}
```

**요청 헤더:**
```
Origin: http://localhost:3000
```

**응답:**
```json
{
  "name": "홍길동",
  "email": "user@example.com",
  "profileImageUrl": "https://...",
  "naverId": "1234567890"
}
```

**응답 헤더:**
```
Authorization: Bearer {ACCESS_TOKEN}
X-Refresh-Token: {REFRESH_TOKEN}
```

### 3. 애플 로그인

```
POST /perfacto/auth/apple-login
```

**요청 파라미터:**
```
code: {AUTHORIZATION_CODE}
id_token: {ID_TOKEN}
user: {USER_INFO_JSON}  // 첫 로그인 시에만
state: {STATE}
```

**응답:**
```json
{
  "name": "Apple User",
  "email": "user@privaterelay.appleid.com",
  "appleId": "001234.abc..."
}
```

**응답 헤더:**
```
Authorization: Bearer {ACCESS_TOKEN}
X-Refresh-Token: {REFRESH_TOKEN}
```

---

## 🔐 JWT 토큰 사용

### 인증이 필요한 API 호출

```http
GET /perfacto/api/some-protected-endpoint
Authorization: Bearer {ACCESS_TOKEN}
```

### Refresh Token으로 토큰 갱신

```http
POST /perfacto/auth/refresh
X-Refresh-Token: {REFRESH_TOKEN}
```

---

## 📊 데이터베이스 스키마

### User 테이블 구조

```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    kakao_id BIGINT,              -- 카카오 고유 ID
    apple_id VARCHAR(255),        -- 애플 고유 ID
    naver_id VARCHAR(255),        -- 네이버 고유 ID
    name VARCHAR(200),            -- 사용자 이름
    nick_name VARCHAR(30),        -- 닉네임
    email VARCHAR(30),            -- 이메일
    profile_image_url TEXT,       -- 프로필 이미지 URL
    tier VARCHAR(50),             -- 사용자 등급
    gender VARCHAR(10),           -- 성별
    birth VARCHAR(20),            -- 생년월일
    point INT DEFAULT 0,          -- 포인트
    count INT DEFAULT 0,          -- 리뷰 작성 횟수
    role VARCHAR(20),             -- 권한 (ROLE_USER)
    created_at TIMESTAMP,         -- 생성 시간
    updated_at TIMESTAMP          -- 수정 시간
);
```

---

## 🛡️ 보안 설정

### SecurityConfig.java

- JWT 토큰 기반 인증
- CSRF 비활성화 (JWT 사용으로 불필요)
- Stateless 세션 관리
- `/perfacto/auth/**` 경로는 인증 없이 접근 가능
- `/perfacto/api/**` 경로는 인증 필요

---

## 🧪 테스트 방법

### 1. 프론트엔드에서 소셜 로그인 버튼 구현

```javascript
// 카카오 로그인
const kakaoLogin = () => {
  window.location.href = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code`;
};

// 네이버 로그인
const naverLogin = () => {
  const state = Math.random().toString(36).substring(7);
  window.location.href = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${NAVER_CLIENT_ID}&redirect_uri=${REDIRECT_URI}&state=${state}`;
};

// 애플 로그인 (Apple JS SDK 사용)
AppleID.auth.signIn();
```

### 2. 콜백 처리

```javascript
// URL에서 code 추출
const urlParams = new URLSearchParams(window.location.search);
const code = urlParams.get('code');
const state = urlParams.get('state');  // 네이버만 해당

// 백엔드로 요청
const response = await fetch(`/perfacto/auth/kakao-login?code=${code}`, {
  headers: {
    'Origin': window.location.origin
  }
});

// JWT 토큰 저장
const accessToken = response.headers.get('Authorization');
const refreshToken = response.headers.get('X-Refresh-Token');
localStorage.setItem('accessToken', accessToken);
localStorage.setItem('refreshToken', refreshToken);
```

---

## 📝 주요 파일 목록

### 백엔드 파일

```
src/main/java/org/example/scrd/  (패키지명은 레거시)
├── controller/
│   ├── AuthController.java          # 인증 컨트롤러
│   └── response/
│       ├── KakaoLoginResponse.java  # 카카오 로그인 응답
│       ├── NaverLoginResponse.java  # 네이버 로그인 응답
│       └── AppleLoginResponse.java  # 애플 로그인 응답
├── service/
│   ├── AuthService.java             # 인증 비즈니스 로직
│   ├── KakaoService.java            # 카카오 OAuth 처리
│   ├── NaverService.java            # 네이버 OAuth 처리
│   └── AppleService.java            # 애플 Sign In 처리
├── domain/
│   └── User.java                    # 사용자 엔티티
├── dto/
│   └── UserDto.java                 # 사용자 DTO
├── repo/
│   └── UserRepository.java          # 사용자 레포지토리
├── config/
│   └── SecurityConfig.java          # Spring Security 설정
├── filter/
│   └── JwtTokenFilter.java          # JWT 필터
└── util/
    └── JwtUtil.java                 # JWT 유틸리티
```

---

## ❗ 주의사항

1. **프로덕션 환경에서는 반드시 HTTPS 사용**
   - Redirect URI를 `https://`로 변경
   - 소셜 로그인 제공자에서 HTTPS URL 등록

2. **JWT Secret Key 보안**
   - 환경 변수로 관리 권장
   - 충분히 긴 랜덤 문자열 사용 (최소 256bit)

3. **애플 로그인 키 파일 보안**
   - `.p8` 파일은 반드시 `.gitignore`에 추가
   - 프로덕션에서는 암호화된 저장소 사용

4. **Refresh Token 보안**
   - Redis에 저장하여 관리
   - 탈취 시 무효화 가능

5. **CORS 설정 확인**
   - 프론트엔드 도메인을 허용 목록에 추가

---

## 🐛 트러블슈팅

### 카카오 로그인 실패
- Redirect URI가 정확히 일치하는지 확인
- 동의 항목이 필수로 설정되어 있는지 확인

### 네이버 로그인 실패
- Client Secret이 올바른지 확인
- state 파라미터가 전달되는지 확인

### 애플 로그인 실패
- `.p8` 파일 경로가 올바른지 확인
- Team ID, Key ID, Client ID가 정확한지 확인
- Services ID의 Return URLs 설정 확인

### JWT 토큰 검증 실패
- Secret Key가 일치하는지 확인
- 토큰 만료 시간 확인
- Bearer 토큰 형식 확인 (`Bearer {token}`)

---

## 📚 참고 문서

- [카카오 로그인 REST API](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api)
- [네이버 로그인 API](https://developers.naver.com/docs/login/api/api.md)
- [Apple Sign In](https://developer.apple.com/documentation/sign_in_with_apple)
- [JWT 공식 문서](https://jwt.io/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)

---

## 🎉 완료!

이제 카카오, 네이버, 애플 소셜 로그인이 JWT 토큰 기반으로 완벽하게 구현되었습니다!
