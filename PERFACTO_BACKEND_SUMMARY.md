# Perfacto Backend Server - 전체 요약

## 🎯 프로젝트 개요

**Perfacto**는 포항 지역의 맛집, 숙박, 카페, 관광지 정보를 제공하는 Flutter 앱을 위한 Spring Boot 백엔드 서버입니다.

### 주요 기능
✅ 장소 정보 관리 (CRUD)
✅ 카테고리별 필터링
✅ 위치 기반 검색 (Haversine 공식)
✅ 좋아요/북마크 시스템
✅ 리뷰 및 평점 시스템
✅ 포인트 보상 시스템
✅ 이미지 업로드
✅ JWT 기반 인증
✅ 카카오 로그인 연동

---

## 📁 프로젝트 구조

```
perfacto_server/
├── src/main/java/org/example/scrd/
│   ├── config/               # 설정 클래스
│   │   ├── SecurityConfig.java
│   │   ├── DataInitializer.java
│   │   ├── WebConfig.java
│   │   ├── MongoConfig.java
│   │   ├── RedisConfig.java
│   │   └── QuerydslConfig.java
│   │
│   ├── controller/           # REST API 컨트롤러
│   │   ├── PlaceController.java           # 장소 API
│   │   ├── CategoryController.java        # 카테고리 API
│   │   ├── LikeController.java           # 좋아요 API
│   │   ├── BookmarkController.java       # 북마크 API
│   │   ├── PerfactoReviewController.java # 리뷰 API
│   │   ├── FileUploadController.java     # 파일 업로드 API
│   │   └── AuthController.java           # 인증 API (기존)
│   │
│   ├── domain/               # 엔티티
│   │   ├── Place.java                    # 장소
│   │   ├── Category.java                 # 카테고리
│   │   ├── PlaceImage.java              # 장소 이미지
│   │   ├── Like.java                     # 좋아요
│   │   ├── Bookmark.java                 # 북마크
│   │   ├── PerfactoReview.java          # 리뷰
│   │   └── User.java                     # 사용자 (기존)
│   │
│   ├── dto/                  # 데이터 전송 객체
│   │   ├── request/
│   │   │   ├── PlaceCreateRequest.java
│   │   │   ├── PlaceUpdateRequest.java
│   │   │   └── ReviewCreateRequest.java
│   │   └── response/
│   │       ├── PlaceResponse.java
│   │       ├── CategoryResponse.java
│   │       ├── ReviewResponse.java
│   │       └── UserSimpleResponse.java
│   │
│   ├── repo/                 # 레포지토리
│   │   ├── PlaceRepository.java
│   │   ├── CategoryRepository.java
│   │   ├── PlaceImageRepository.java
│   │   ├── LikeRepository.java
│   │   ├── BookmarkRepository.java
│   │   ├── PerfactoReviewRepository.java
│   │   └── UserRepository.java (기존)
│   │
│   ├── service/              # 비즈니스 로직
│   │   ├── PlaceService.java
│   │   ├── CategoryService.java
│   │   ├── LikeService.java
│   │   ├── BookmarkService.java
│   │   ├── PerfactoReviewService.java
│   │   └── FileUploadService.java
│   │
│   ├── exception/            # 예외 처리
│   │   ├── NotFoundException.java
│   │   ├── UnauthorizedAccessException.java
│   │   ├── AlreadyJoinedException.java
│   │   └── GlobalExceptionHandler.java
│   │
│   ├── filter/               # 필터
│   │   ├── JwtTokenFilter.java
│   │   └── ExceptionHandlerFilter.java
│   │
│   ├── util/                 # 유틸리티
│   │   └── JwtUtil.java
│   │
│   ├── BaseEntity.java       # 공통 엔티티
│   └── ScrdApplication.java  # 메인 클래스
│
├── src/main/resources/
│   └── application.yml       # 설정 파일
│
├── build.gradle              # Gradle 빌드 설정
├── PERFACTO_API_GUIDE.md     # 📘 API 문서
├── SETUP_GUIDE.md            # ⚙️ 설치 가이드
└── PERFACTO_BACKEND_SUMMARY.md # 📋 이 파일
```

---

## 🗄️ 데이터베이스 스키마

### 주요 테이블 (새로 생성)

| 테이블 | 설명 | 주요 컬럼 |
|--------|------|-----------|
| `places` | 장소 정보 | id, name, district, latitude, longitude, category_id |
| `categories` | 카테고리 | id, code, name, display_order |
| `place_images` | 장소 이미지 | id, place_id, image_url, display_order |
| `likes` | 좋아요 | id, user_id, place_id |
| `bookmarks` | 북마크 | id, user_id, place_id, memo |
| `perfacto_reviews` | 리뷰 | id, place_id, user_id, rating, content |

### 기존 테이블 (활용)
- `user` - 사용자 정보
- `refresh_token` - 리프레시 토큰 (Redis)

---

## 🔌 API 엔드포인트 요약

### 인증 (기존 활용)
```
GET  /perfacto/auth/kakao-login        # 카카오 로그인
POST /perfacto/auth/refresh            # 토큰 갱신
```

### 장소 (Place)
```
POST   /perfacto/api/places            # 장소 생성
GET    /perfacto/api/places/{id}       # 장소 상세
PUT    /perfacto/api/places/{id}       # 장소 수정
DELETE /perfacto/api/places/{id}       # 장소 삭제
GET    /perfacto/api/places            # 전체 조회
GET    /perfacto/api/places/category/{categoryId}   # 카테고리별
GET    /perfacto/api/places/district/{district}     # 구역별
GET    /perfacto/api/places/search?keyword=         # 검색
GET    /perfacto/api/places/hot                     # HOT 장소
GET    /perfacto/api/places/recent                  # 최신 장소
GET    /perfacto/api/places/nearby?lat=&lon=        # 주변 장소
GET    /perfacto/api/places/my                      # 내가 등록한 장소
```

### 카테고리 (Category)
```
GET /perfacto/every/categories                      # 전체 카테고리
GET /perfacto/every/categories/{id}                 # 카테고리 상세
```

### 좋아요 (Like)
```
POST   /perfacto/api/likes/{placeId}                # 좋아요 추가
DELETE /perfacto/api/likes/{placeId}                # 좋아요 취소
GET    /perfacto/api/likes/{placeId}/check          # 좋아요 여부
GET    /perfacto/api/likes/my                       # 내 좋아요 목록
```

### 북마크 (Bookmark)
```
POST   /perfacto/api/bookmarks/{placeId}            # 북마크 추가
DELETE /perfacto/api/bookmarks/{placeId}            # 북마크 제거
PUT    /perfacto/api/bookmarks/{placeId}/memo       # 메모 수정
GET    /perfacto/api/bookmarks/{placeId}/check      # 북마크 여부
GET    /perfacto/api/bookmarks/my                   # 내 북마크 목록
```

### 리뷰 (Review)
```
POST   /perfacto/api/reviews                        # 리뷰 작성
PUT    /perfacto/api/reviews/{id}                   # 리뷰 수정
DELETE /perfacto/api/reviews/{id}                   # 리뷰 삭제
GET    /perfacto/api/reviews/place/{placeId}        # 장소별 리뷰
GET    /perfacto/api/reviews/my                     # 내 리뷰
POST   /perfacto/api/reviews/{id}/helpful           # 도움이 됨
```

### 파일 업로드 (Upload)
```
POST   /perfacto/api/upload/place/{placeId}         # 장소 이미지
POST   /perfacto/api/upload/profile                 # 프로필 이미지
DELETE /perfacto/api/upload/{imageId}               # 이미지 삭제
```

---

## 🔐 보안 및 인증

### JWT 토큰 기반 인증
- **Access Token**: 2시간 유효
- **Refresh Token**: 2시간 유효 (Redis 저장)
- **Header**: `Authorization: Bearer {token}`

### 권한 구분
| 경로 | 권한 |
|------|------|
| `/perfacto/auth/**` | 공개 (인증 불필요) |
| `/perfacto/every/**` | 공개 (카테고리 조회 등) |
| `/perfacto/api/**` | 인증 필요 |

---

## 💎 포인트 시스템

### 포인트 획득
- 장소 등록: **+100 포인트**
- 리뷰 작성: **+500 포인트**

### 사용자 등급 (Tier)
기존 SCRD 시스템 활용:
- 리뷰 작성 횟수에 따라 자동 등급 상승

---

## 🚀 시작하기

### 1. 데이터베이스 생성
```sql
CREATE DATABASE perfacto CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 설정 파일 수정
`src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/perfacto
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: create  # 최초 실행 시, 이후 validate로 변경
```

### 3. 서버 실행
```bash
./gradlew bootRun
```

서버 주소: `http://localhost:8080`

### 4. 초기 데이터 확인
서버 시작 시 자동으로 4개 카테고리 생성:
- restaurant (음식점)
- accommodation (숙박)
- cafe (카페)
- attraction (관광지)

확인:
```bash
curl http://localhost:8080/perfacto/every/categories
```

---

## 📱 Flutter 앱 연동

### 1. Base URL 설정
```dart
class ApiConfig {
  static const String baseUrl = 'http://localhost:8080';
  static const String apiPrefix = '/perfacto/api';
}
```

### 2. 장소 목록 조회 예제
```dart
Future<List<Place>> getPlaces({
  int page = 0,
  int size = 20,
  String? categoryCode,
  String? district,
}) async {
  String url = '${ApiConfig.baseUrl}${ApiConfig.apiPrefix}/places';

  // 카테고리 필터
  if (categoryCode != null) {
    final category = await getCategoryByCode(categoryCode);
    url = '${ApiConfig.baseUrl}${ApiConfig.apiPrefix}/places/category/${category.id}';
  }

  // 구역 필터
  if (district != null) {
    url = '${ApiConfig.baseUrl}${ApiConfig.apiPrefix}/places/district/$district';
  }

  final response = await http.get(
    Uri.parse('$url?page=$page&size=$size'),
    headers: {'Content-Type': 'application/json'},
  );

  if (response.statusCode == 200) {
    final data = json.decode(response.body);
    return (data['data']['content'] as List)
        .map((json) => Place.fromJson(json))
        .toList();
  }
  throw Exception('Failed to load places');
}
```

### 3. HOT 장소 조회
```dart
Future<List<Place>> getHotPlaces({int limit = 10}) async {
  final response = await http.get(
    Uri.parse('${ApiConfig.baseUrl}${ApiConfig.apiPrefix}/places/hot?limit=$limit'),
  );

  if (response.statusCode == 200) {
    final data = json.decode(response.body);
    return (data['data'] as List)
        .map((json) => Place.fromJson(json))
        .toList();
  }
  throw Exception('Failed to load hot places');
}
```

### 4. 위치 기반 검색
```dart
Future<List<Place>> getNearbyPlaces({
  required double lat,
  required double lon,
  double radius = 5.0,
}) async {
  final response = await http.get(
    Uri.parse(
      '${ApiConfig.baseUrl}${ApiConfig.apiPrefix}/places/nearby'
      '?lat=$lat&lon=$lon&radius=$radius'
    ),
  );

  if (response.statusCode == 200) {
    final data = json.decode(response.body);
    return (data['data'] as List)
        .map((json) => Place.fromJson(json))
        .toList();
  }
  throw Exception('Failed to load nearby places');
}
```

---

## 🏗️ 기술 스택

### Backend
- **Framework**: Spring Boot 3.3.5
- **Language**: Java 17
- **Build Tool**: Gradle 8.10.2

### Database
- **Primary**: MySQL 8.0 (장소, 사용자, 리뷰 등)
- **Cache**: Redis (Refresh Token)
- **Document**: MongoDB (선택사항)

### Security & Auth
- **Authentication**: JWT (JSON Web Token)
- **Social Login**: Kakao, Naver, Apple
- **Security**: Spring Security

### ORM & Query
- **ORM**: JPA/Hibernate
- **Query Builder**: QueryDSL
- **Auditing**: Spring Data JPA Auditing

---

## 📊 주요 기능 상세

### 1. 위치 기반 검색
**Haversine Formula** 사용으로 정확한 거리 계산:
```sql
SELECT * FROM places
WHERE (6371 * acos(cos(radians(?)) * cos(radians(latitude)) *
      cos(radians(longitude) - radians(?)) +
      sin(radians(?)) * sin(radians(latitude)))) <= ?
```

### 2. 좋아요/북마크 시스템
- 중복 방지 (Unique 제약조건)
- 카운트 자동 업데이트
- 삭제 시 @PreRemove로 카운트 감소

### 3. 리뷰 시스템
- 1.0 ~ 5.0 평점
- 평균 평점 자동 계산
- 도움이 됨 기능
- 한 장소당 사용자 1개 리뷰만 허용

### 4. 이미지 업로드
- 로컬 스토리지 저장
- UUID 기반 파일명
- Display Order로 대표 이미지 설정
- 향후 S3 연동 가능

---

## 🔄 Firebase에서 Spring Boot로 마이그레이션

### 데이터 마이그레이션
1. Firebase Firestore 데이터 Export
2. JSON → MySQL 변환 스크립트 작성
3. 데이터 Import

### Flutter 앱 수정사항
1. Firebase SDK 제거 또는 병행 사용
2. API 호출 코드 변경
3. 인증 방식 변경 (Firebase Auth → JWT)
4. 이미지 경로 변경

---

## 📚 참고 문서

- **API 전체 문서**: `PERFACTO_API_GUIDE.md`
- **설치 가이드**: `SETUP_GUIDE.md`
- **소셜 로그인**: `SOCIAL_LOGIN_GUIDE.md` (기존)

---

## 🎓 학습 및 개발 팁

### 1. API 테스트
- **Postman** 사용 권장
- 카테고리 조회부터 시작
- 인증 토큰은 카카오 로그인으로 획득

### 2. 데이터베이스 관리
- **DBeaver** 또는 **MySQL Workbench** 사용
- 초기에는 `ddl-auto: create` 사용
- 안정화 후 `validate`로 변경

### 3. 로그 확인
```yaml
logging:
  level:
    org.example.scrd: DEBUG  # 상세 로그
```

### 4. 디버깅
- IntelliJ IDEA Debugger 활용
- Breakpoint 설정
- Service 레이어부터 디버깅 시작

---

## 🚧 향후 개선 사항

### 기능 확장
- [ ] 장소 신고 기능
- [ ] 사용자 팔로우 시스템
- [ ] 장소 태그 시스템
- [ ] 검색 필터 고도화

### 인프라
- [ ] S3 이미지 업로드
- [ ] Redis 캐싱 확대
- [ ] Elasticsearch 검색 연동
- [ ] CI/CD 파이프라인

### 모니터링
- [ ] Spring Actuator 활성화
- [ ] Prometheus + Grafana
- [ ] 로그 집계 (ELK Stack)

---

## 💡 문제 해결

### Q: 서버가 실행되지 않아요
**A**:
1. MySQL이 실행 중인지 확인
2. `application.yml`의 DB 설정 확인
3. 포트 충돌 확인 (기본 8080)

### Q: API 호출 시 401 Unauthorized
**A**:
1. Access Token 확인
2. Header 형식 확인: `Authorization: Bearer {token}`
3. Token 만료 확인 (2시간)

### Q: 이미지 업로드 실패
**A**:
1. `/uploads` 디렉토리 존재 확인
2. 파일 크기 확인 (최대 10MB)
3. 권한 확인 (`chmod 755 /uploads`)

---

## 📞 지원

### 개발 문의
- GitHub Issues 등록
- 상세한 에러 로그 첨부

### 기여하기
1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

---

## 📄 라이센스

MIT License

---

**Happy Coding! 🚀**
