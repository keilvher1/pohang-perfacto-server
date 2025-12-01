# 프로젝트 이름 변경 완료 안내

## 📋 변경 사항

프로젝트 이름이 **SCRD**에서 **Perfacto Server**로 변경되었습니다.

---

## ✅ 변경된 파일 목록

### 1. **프로젝트 설정 파일**

#### `settings.gradle`
```gradle
rootProject.name = 'perfacto_server'  # 변경됨 (기존: 'scrd')
```

#### `build.gradle`
```gradle
group = 'com.perfacto'  # 변경됨 (기존: 'org.example')
```

---

### 2. **설정 파일**

#### `application.yml`
모든 Redirect URI가 변경되었습니다:

- **카카오**: `http://localhost:8080/api/perfacto/auth/kakao-login`
- **네이버**: `http://localhost:8080/api/perfacto/auth/naver-login`
- **애플**: `http://localhost:8080/api/perfacto/auth/apple-login`

---

### 3. **Spring Security 설정**

#### `SecurityConfig.java`
인증 경로 패턴이 변경되었습니다:

```java
.requestMatchers("/perfacto/auth/**", "/error" ,"/").permitAll()
.requestMatchers("/perfacto/every/**").permitAll()
.requestMatchers("/perfacto/api/**").authenticated()
```

---

### 4. **모든 Controller 경로**

| Controller | 변경 전 | 변경 후 |
|-----------|---------|---------|
| AuthController (카카오) | `/scrd/auth/kakao-login` | `/perfacto/auth/kakao-login` |
| AuthController (네이버) | `/scrd/auth/naver-login` | `/perfacto/auth/naver-login` |
| AuthController (애플) | `/scrd/auth/apple-login` | `/perfacto/auth/apple-login` |
| PartyCommentController | `/scrd/api/party/comment` | `/perfacto/api/party/comment` |
| ThemeController | `/scrd/api` | `/perfacto/api` |
| NotificationController | `/scrd/api` | `/perfacto/api` |
| ReviewController | `/scrd/api` | `/perfacto/api` |
| SavedThemeController | `/scrd/api/save` | `/perfacto/api/save` |
| PartyController | `/scrd/api/party` | `/perfacto/api/party` |
| UserController | `/scrd/api/user` | `/perfacto/api/user` |

---

### 5. **문서 파일**

- `SOCIAL_LOGIN_GUIDE.md`: 모든 API 경로 및 설명이 업데이트됨
- `README.md`: 프로젝트 제목에 "Perfacto Server" 추가

---

## 🔧 필수 작업 사항

### 1. **소셜 로그인 설정 업데이트**

각 소셜 로그인 제공자의 개발자 콘솔에서 Redirect URI를 업데이트해야 합니다:

#### 카카오 개발자센터
- [Kakao Developers](https://developers.kakao.com/)
- Redirect URI: `http://localhost:8080/api/perfacto/auth/kakao-login`
- (프로덕션) Redirect URI: `https://your-domain.com/api/perfacto/auth/kakao-login`

#### 네이버 개발자센터
- [네이버 개발자센터](https://developers.naver.com/)
- Callback URL: `http://localhost:8080/api/perfacto/auth/naver-login`
- (프로덕션) Callback URL: `https://your-domain.com/api/perfacto/auth/naver-login`

#### Apple Developer
- [Apple Developer](https://developer.apple.com/)
- Return URLs: `http://localhost:8080/api/perfacto/auth/apple-login`
- (프로덕션) Return URLs: `https://your-domain.com/api/perfacto/auth/apple-login`

---

### 2. **프론트엔드 API 경로 업데이트**

프론트엔드에서 호출하는 모든 API 경로를 변경해야 합니다:

```javascript
// 변경 전
fetch('/scrd/api/theme')
fetch('/scrd/auth/kakao-login')

// 변경 후
fetch('/perfacto/api/theme')
fetch('/perfacto/auth/kakao-login')
```

---

### 3. **데이터베이스 마이그레이션 (선택사항)**

네이버 로그인 기능을 위해 User 테이블에 컬럼이 추가되었습니다:

```sql
ALTER TABLE user ADD COLUMN naver_id VARCHAR(255);
```

---

## 📦 빌드 및 실행

### 1. 의존성 다운로드
```bash
./gradlew clean build
```

### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```

또는

```bash
java -jar build/libs/app.jar
```

---

## 🧪 테스트

### API 엔드포인트 확인
```bash
# 카카오 로그인 (GET)
curl http://localhost:8080/perfacto/auth/kakao-login?code=YOUR_CODE

# 네이버 로그인 (GET)
curl http://localhost:8080/perfacto/auth/naver-login?code=YOUR_CODE&state=YOUR_STATE

# 애플 로그인 (POST)
curl -X POST http://localhost:8080/perfacto/auth/apple-login \
  -d "code=YOUR_CODE&id_token=YOUR_TOKEN"
```

---

## 📚 참고 문서

- `SOCIAL_LOGIN_GUIDE.md`: 소셜 로그인 상세 구현 가이드
- `README.md`: 프로젝트 전체 개요

---

## ⚠️ 주의사항

1. **패키지명은 변경되지 않았습니다**
   - Java 패키지: `org.example.scrd` (유지)
   - 향후 필요시 패키지 리팩토링 검토

2. **Docker 이미지명**
   - `bootJar` 설정의 `archiveFileName`은 여전히 `app.jar`입니다.
   - 필요시 변경 가능

3. **데이터베이스 연결**
   - `application.yml`의 데이터베이스 연결 정보는 그대로 유지됩니다.

---

## ✨ 완료!

Perfacto Server로의 프로젝트 이름 변경이 완료되었습니다! 🎉
