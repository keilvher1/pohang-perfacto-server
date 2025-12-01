# Perfacto Backend API Documentation

포항 지도 앱 Perfacto의 백엔드 API 문서입니다.

## 목차
- [개요](#개요)
- [기술 스택](#기술-스택)
- [시작하기](#시작하기)
- [API 엔드포인트](#api-엔드포인트)
  - [인증 API](#인증-api)
  - [장소 API](#장소-api)
  - [카테고리 API](#카테고리-api)
  - [좋아요 API](#좋아요-api)
  - [북마크 API](#북마크-api)
  - [리뷰 API](#리뷰-api)
  - [파일 업로드 API](#파일-업로드-api)
- [데이터베이스 스키마](#데이터베이스-스키마)
- [보안](#보안)

---

## 개요

Perfacto는 포항 지역의 맛집, 숙박, 카페, 관광지 정보를 제공하는 모바일 앱의 백엔드 서버입니다.

### 주요 기능
- 장소 정보 관리 (CRUD)
- 카테고리별 필터링
- 위치 기반 검색
- 좋아요/북마크 시스템
- 리뷰 시스템
- 포인트 시스템
- 이미지 업로드
- JWT 기반 인증

---

## 기술 스택

- **Framework**: Spring Boot 3.3.5
- **Language**: Java 17
- **Database**: MySQL 8.0
- **Cache**: Redis
- **Document DB**: MongoDB (선택사항)
- **Security**: Spring Security + JWT
- **ORM**: JPA/Hibernate
- **Query**: QueryDSL
- **Build**: Gradle

---

## 시작하기

### 필수 요구사항
- JDK 17 이상
- MySQL 8.0
- Redis (선택사항)
- Gradle 8.10.2

### 설치 및 실행

1. **저장소 클론**
```bash
cd /Users/mac/spring_boot_proj/perfacto_server
```

2. **데이터베이스 설정**
```sql
CREATE DATABASE perfacto CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **application.yml 설정**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/perfacto
    username: your_username
    password: your_password
```

4. **빌드 및 실행**
```bash
./gradlew clean build
./gradlew bootRun
```

서버는 `http://localhost:8080`에서 실행됩니다.

---

## API 엔드포인트

### 기본 정보
- **Base URL**: `http://localhost:8080`
- **인증 방식**: Bearer Token (JWT)
- **응답 형식**: JSON

### API 응답 구조
```json
{
  "success": true,
  "data": { },
  "message": "Success"
}
```

---

## 인증 API

기존 SCRD 프로젝트의 인증 시스템을 활용합니다.

### 카카오 로그인
```http
GET /perfacto/auth/kakao-login?code={인가코드}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR...",
    "user": {
      "id": 1,
      "nickName": "사용자",
      "profileImageUrl": "https://...",
      "point": 0
    }
  }
}
```

---

## 장소 API

### 1. 장소 생성
```http
POST /perfacto/api/places
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "장소명",
  "description": "장소 설명",
  "district": "남구",
  "address": "포항시 남구 ...",
  "latitude": 36.019,
  "longitude": 129.343,
  "categoryId": 1,
  "phoneNumber": "054-123-4567",
  "website": "https://example.com",
  "businessHours": "09:00-18:00"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "장소명",
    "category": {
      "id": 1,
      "code": "restaurant",
      "name": "음식점"
    },
    "likeCount": 0,
    "bookmarkCount": 0,
    "viewCount": 0,
    "averageRating": 0.0,
    "reviewCount": 0
  },
  "message": "장소가 생성되었습니다."
}
```

### 2. 장소 수정
```http
PUT /perfacto/api/places/{placeId}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "수정된 장소명",
  "description": "수정된 설명"
}
```

### 3. 장소 상세 조회
```http
GET /perfacto/api/places/{placeId}
```

### 4. 장소 삭제
```http
DELETE /perfacto/api/places/{placeId}
Authorization: Bearer {token}
```

### 5. 모든 장소 조회 (페이징)
```http
GET /perfacto/api/places?page=0&size=20&sort=regDate,desc
```

### 6. 카테고리별 장소 조회
```http
GET /perfacto/api/places/category/{categoryId}?page=0&size=20
```

### 7. 구역별 장소 조회
```http
GET /perfacto/api/places/district/{district}?page=0&size=20
```

### 8. 장소명으로 검색
```http
GET /perfacto/api/places/search?keyword=맛집&page=0&size=20
```

### 9. HOT 장소 조회 (좋아요 순)
```http
GET /perfacto/api/places/hot?limit=10
```

### 10. 최신 장소 조회
```http
GET /perfacto/api/places/recent?limit=10
```

### 11. 평점 상위 장소 조회
```http
GET /perfacto/api/places/top-rated?limit=10
```

### 12. 위치 기반 주변 장소 검색
```http
GET /perfacto/api/places/nearby?lat=36.019&lon=129.343&radius=5
```
- `radius`: 반경 (단위: km, 기본값: 5)

### 13. 내가 등록한 장소 조회
```http
GET /perfacto/api/places/my
Authorization: Bearer {token}
```

---

## 카테고리 API

### 1. 모든 카테고리 조회
```http
GET /perfacto/every/categories
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "code": "restaurant",
      "name": "음식점",
      "nameEn": "Restaurant",
      "displayOrder": 1,
      "isActive": true
    },
    {
      "id": 2,
      "code": "accommodation",
      "name": "숙박",
      "nameEn": "Accommodation",
      "displayOrder": 2,
      "isActive": true
    }
  ]
}
```

### 2. 카테고리 ID로 조회
```http
GET /perfacto/every/categories/{categoryId}
```

### 3. 카테고리 코드로 조회
```http
GET /perfacto/every/categories/code/{code}
```

---

## 좋아요 API

### 1. 좋아요 추가
```http
POST /perfacto/api/likes/{placeId}
Authorization: Bearer {token}
```

### 2. 좋아요 취소
```http
DELETE /perfacto/api/likes/{placeId}
Authorization: Bearer {token}
```

### 3. 좋아요 여부 확인
```http
GET /perfacto/api/likes/{placeId}/check
Authorization: Bearer {token}
```

**Response:**
```json
{
  "success": true,
  "data": true
}
```

### 4. 내가 좋아요한 장소 목록
```http
GET /perfacto/api/likes/my?page=0&size=20
Authorization: Bearer {token}
```

---

## 북마크 API

### 1. 북마크 추가
```http
POST /perfacto/api/bookmarks/{placeId}?memo=나중에 가볼 곳
Authorization: Bearer {token}
```

### 2. 북마크 제거
```http
DELETE /perfacto/api/bookmarks/{placeId}
Authorization: Bearer {token}
```

### 3. 북마크 메모 업데이트
```http
PUT /perfacto/api/bookmarks/{placeId}/memo?memo=꼭 가봐야 할 곳
Authorization: Bearer {token}
```

### 4. 북마크 여부 확인
```http
GET /perfacto/api/bookmarks/{placeId}/check
Authorization: Bearer {token}
```

### 5. 내가 북마크한 장소 목록
```http
GET /perfacto/api/bookmarks/my?page=0&size=20
Authorization: Bearer {token}
```

---

## 리뷰 API

### 1. 리뷰 작성
```http
POST /perfacto/api/reviews
Authorization: Bearer {token}
Content-Type: application/json

{
  "placeId": 1,
  "rating": 4.5,
  "content": "음식이 정말 맛있었어요!"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "placeId": 1,
    "placeName": "장소명",
    "user": {
      "id": 1,
      "nickName": "사용자",
      "profileImageUrl": "https://..."
    },
    "rating": 4.5,
    "content": "음식이 정말 맛있었어요!",
    "helpfulCount": 0,
    "regDate": "2025-01-01T12:00:00"
  },
  "message": "리뷰가 작성되었습니다."
}
```

> **보상**: 리뷰 작성 시 500 포인트 지급

### 2. 리뷰 수정
```http
PUT /perfacto/api/reviews/{reviewId}?rating=5.0&content=수정된 리뷰
Authorization: Bearer {token}
```

### 3. 리뷰 삭제
```http
DELETE /perfacto/api/reviews/{reviewId}
Authorization: Bearer {token}
```

### 4. 장소별 리뷰 조회
```http
GET /perfacto/api/reviews/place/{placeId}?page=0&size=20
```

### 5. 내가 작성한 리뷰 조회
```http
GET /perfacto/api/reviews/my?page=0&size=20
Authorization: Bearer {token}
```

### 6. 도움이 됨 증가
```http
POST /perfacto/api/reviews/{reviewId}/helpful
```

### 7. 도움이 됨 수 상위 리뷰 조회
```http
GET /perfacto/api/reviews/place/{placeId}/top-helpful?limit=5
```

---

## 파일 업로드 API

### 1. 장소 이미지 업로드
```http
POST /perfacto/api/upload/place/{placeId}
Authorization: Bearer {token}
Content-Type: multipart/form-data

file: (binary)
displayOrder: 0  # 0이 대표 이미지
```

**Response:**
```json
{
  "success": true,
  "data": "/files/uuid-filename.jpg",
  "message": "이미지가 업로드되었습니다."
}
```

### 2. 프로필 이미지 업로드
```http
POST /perfacto/api/upload/profile
Authorization: Bearer {token}
Content-Type: multipart/form-data

file: (binary)
```

### 3. 이미지 삭제
```http
DELETE /perfacto/api/upload/{imageId}
Authorization: Bearer {token}
```

---

## 데이터베이스 스키마

### 주요 테이블

#### places (장소)
```sql
CREATE TABLE places (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  description TEXT,
  district VARCHAR(100) NOT NULL,
  address VARCHAR(500) NOT NULL,
  latitude DOUBLE NOT NULL,
  longitude DOUBLE NOT NULL,
  category_id BIGINT NOT NULL,
  phone_number VARCHAR(20),
  website VARCHAR(500),
  business_hours VARCHAR(100),
  like_count INT DEFAULT 0,
  bookmark_count INT DEFAULT 0,
  view_count INT DEFAULT 0,
  is_active BOOLEAN DEFAULT TRUE,
  created_by BIGINT,
  reg_date DATETIME,
  mod_date DATETIME,
  FOREIGN KEY (category_id) REFERENCES categories(id),
  FOREIGN KEY (created_by) REFERENCES user(id),
  INDEX idx_category (category_id),
  INDEX idx_district (district)
);
```

#### categories (카테고리)
```sql
CREATE TABLE categories (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) UNIQUE NOT NULL,
  name VARCHAR(100) NOT NULL,
  name_en VARCHAR(100),
  description TEXT,
  icon_url VARCHAR(500),
  display_order INT DEFAULT 0,
  is_active BOOLEAN DEFAULT TRUE,
  reg_date DATETIME,
  mod_date DATETIME
);
```

#### likes (좋아요)
```sql
CREATE TABLE likes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  place_id BIGINT NOT NULL,
  reg_date DATETIME,
  UNIQUE KEY uk_user_place (user_id, place_id),
  FOREIGN KEY (user_id) REFERENCES user(id),
  FOREIGN KEY (place_id) REFERENCES places(id)
);
```

#### bookmarks (북마크)
```sql
CREATE TABLE bookmarks (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  place_id BIGINT NOT NULL,
  memo VARCHAR(500),
  reg_date DATETIME,
  UNIQUE KEY uk_user_place (user_id, place_id),
  FOREIGN KEY (user_id) REFERENCES user(id),
  FOREIGN KEY (place_id) REFERENCES places(id)
);
```

#### perfacto_reviews (리뷰)
```sql
CREATE TABLE perfacto_reviews (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  place_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  rating DOUBLE NOT NULL,
  content TEXT,
  helpful_count INT DEFAULT 0,
  is_active BOOLEAN DEFAULT TRUE,
  reg_date DATETIME,
  mod_date DATETIME,
  FOREIGN KEY (place_id) REFERENCES places(id),
  FOREIGN KEY (user_id) REFERENCES user(id),
  INDEX idx_place_id (place_id),
  INDEX idx_rating (rating)
);
```

---

## 보안

### JWT 토큰
- Access Token: 2시간 유효
- Refresh Token: 2시간 유효 (Redis 저장)

### 인증이 필요한 엔드포인트
- `/perfacto/api/**`: 모든 API 요청
  - 예외: 장소 조회, 카테고리 조회 등 일부 GET 요청

### 인증이 불필요한 엔드포인트
- `/perfacto/auth/**`: 인증 관련
- `/perfacto/every/**`: 공개 데이터
- `/files/**`: 업로드된 파일 서빙

### CORS 설정
- 모든 Origin 허용 (개발 환경)
- 프로덕션에서는 특정 도메인만 허용 필요

---

## 포인트 시스템

### 포인트 획득
- 장소 등록: **+100 포인트**
- 리뷰 작성: **+500 포인트**

### 향후 확장
- 포인트로 배지 획득
- 포인트 랭킹 시스템
- 포인트 사용 기능

---

## 에러 코드

| HTTP Status | 설명 |
|------------|------|
| 200 | 성공 |
| 201 | 생성 성공 |
| 400 | 잘못된 요청 |
| 401 | 인증 실패 |
| 403 | 권한 없음 |
| 404 | 리소스를 찾을 수 없음 |
| 409 | 중복 요청 (이미 좋아요, 이미 리뷰 작성 등) |
| 500 | 서버 오류 |

---

## Flutter 앱 연동 가이드

### 1. 기본 설정
```dart
class ApiService {
  static const String baseUrl = 'http://localhost:8080';
  String? accessToken;

  // 헤더 설정
  Map<String, String> get headers => {
    'Content-Type': 'application/json',
    if (accessToken != null) 'Authorization': 'Bearer $accessToken',
  };
}
```

### 2. 장소 목록 조회 예제
```dart
Future<List<Place>> getPlaces() async {
  final response = await http.get(
    Uri.parse('$baseUrl/perfacto/api/places'),
    headers: headers,
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

### 3. 장소 생성 예제
```dart
Future<Place> createPlace(PlaceCreateRequest request) async {
  final response = await http.post(
    Uri.parse('$baseUrl/perfacto/api/places'),
    headers: headers,
    body: json.encode(request.toJson()),
  );

  if (response.statusCode == 201) {
    final data = json.decode(response.body);
    return Place.fromJson(data['data']);
  }
  throw Exception('Failed to create place');
}
```

### 4. 이미지 업로드 예제
```dart
Future<String> uploadImage(File imageFile, int placeId) async {
  var request = http.MultipartRequest(
    'POST',
    Uri.parse('$baseUrl/perfacto/api/upload/place/$placeId'),
  );

  request.headers['Authorization'] = 'Bearer $accessToken';
  request.files.add(await http.MultipartFile.fromPath(
    'file',
    imageFile.path,
  ));

  final response = await request.send();
  if (response.statusCode == 200) {
    final responseData = await response.stream.bytesToString();
    final data = json.decode(responseData);
    return data['data']; // 이미지 URL 반환
  }
  throw Exception('Failed to upload image');
}
```

---

## 배포

### 1. JAR 파일 빌드
```bash
./gradlew bootJar
```

### 2. 실행
```bash
java -jar build/libs/app.jar
```

### 3. 프로덕션 설정
- `application-prod.yml` 생성
- 환경 변수로 민감한 정보 관리
```bash
export JWT_SECRET=your-secret-key
export DB_PASSWORD=your-db-password
java -jar -Dspring.profiles.active=prod app.jar
```

---

## 라이센스

MIT License

---

## 문의

이슈 및 문의사항은 GitHub Issues에 등록해주세요.
