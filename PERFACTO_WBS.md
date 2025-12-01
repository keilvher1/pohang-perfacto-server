# Perfacto 프로젝트 WBS (Work Breakdown Structure)

> 이 문서를 Notion에 복사-붙여넣기하세요. Notion에서 Markdown import를 지원합니다.

---

## 📋 프로젝트 개요

**프로젝트명**: Perfacto (포항 지도 앱)
**목표**: Spring Boot 백엔드 서버 배포 및 Flutter 앱 연동
**기간**: 약 2-3주
**배포 환경**: AWS Free Tier → 유료 전환

---

## 🎯 Phase 1: 서버 실행 및 테스트 (1-2일)

### 1.1 로컬 환경 설정
- [ ] MySQL 8.0 설치 확인
- [ ] JDK 17 설치 확인
- [ ] IntelliJ IDEA 또는 VSCode 설정

### 1.2 데이터베이스 설정
- [ ] MySQL 서버 실행
- [ ] `perfacto` 데이터베이스 생성
```sql
CREATE DATABASE perfacto CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
- [ ] 사용자 생성 및 권한 부여 (선택사항)
- [ ] `application.yml` 파일에 DB 접속 정보 입력
  - username
  - password
  - url

### 1.3 서버 빌드 및 실행
- [ ] 프로젝트 디렉토리 이동
```bash
cd /Users/mac/spring_boot_proj/perfacto_server
```
- [ ] Gradle 빌드 실행
```bash
./gradlew clean build
```
- [ ] 서버 실행
```bash
./gradlew bootRun
```
- [ ] 서버 정상 실행 확인 (localhost:8080)

### 1.4 기본 API 테스트
- [ ] Postman 또는 cURL 설치
- [ ] 카테고리 조회 테스트
```bash
curl http://localhost:8080/perfacto/every/categories
```
- [ ] 응답 확인 (4개 카테고리 자동 생성 확인)
  - restaurant (음식점)
  - accommodation (숙박)
  - cafe (카페)
  - attraction (관광지)

### 1.5 테스트 데이터 생성
- [ ] Postman Collection 생성
- [ ] 소셜 로그인 테스트 (카카오)
- [ ] 장소 생성 API 테스트
- [ ] 좋아요/북마크 테스트
- [ ] 리뷰 작성 테스트
- [ ] 이미지 업로드 테스트

**산출물**:
- ✅ 로컬에서 실행되는 백엔드 서버
- ✅ Postman API 테스트 컬렉션
- ✅ 테스트 데이터 (장소 5-10개)

---

## 🔑 Phase 2: 소셜 로그인 키 발급 및 연결 (1-2일)

### 2.1 카카오 로그인 설정
- [ ] [Kakao Developers](https://developers.kakao.com) 접속
- [ ] 카카오 계정으로 로그인
- [ ] 새 애플리케이션 생성
  - 앱 이름: Perfacto
  - 사업자명: (본인 이름 또는 회사명)
- [ ] REST API 키 복사
- [ ] 플랫폼 설정
  - [ ] Android 추가
    - 패키지명: `com.perfacto.app` (Flutter 앱의 패키지명)
    - 키 해시 등록
  - [ ] iOS 추가
    - Bundle ID: `com.perfacto.app`
- [ ] Redirect URI 설정
  - 개발: `http://localhost:8080/perfacto/auth/kakao-login`
  - 프로덕션: `https://api.perfacto.com/perfacto/auth/kakao-login`
- [ ] 동의 항목 설정
  - 닉네임 (필수)
  - 프로필 사진 (선택)
  - 카카오계정 (이메일) (선택)
- [ ] `application.yml`에 REST API 키 입력
```yaml
kakao:
  api:
    key:
      client: "발급받은_REST_API_KEY"
```

**참고 문서**: Flutter 앱에서 키 해시 생성 방법
```bash
# Android Debug Key Hash
keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64

# iOS는 Bundle ID만 등록
```

### 2.2 네이버 로그인 설정 (선택사항)
- [ ] [네이버 개발자센터](https://developers.naver.com) 접속
- [ ] 네이버 계정으로 로그인
- [ ] 애플리케이션 등록
  - 애플리케이션 이름: Perfacto
  - 사용 API: 네이버 로그인
- [ ] Client ID, Client Secret 복사
- [ ] 서비스 환경 추가
  - [ ] Android 앱 패키지명: `com.perfacto.app`
  - [ ] iOS URL Scheme: `perfacto`
- [ ] Callback URL 설정
  - 개발: `http://localhost:8080/perfacto/auth/naver-login`
  - 프로덕션: `https://api.perfacto.com/perfacto/auth/naver-login`
- [ ] 제공 정보 선택
  - 이름 (필수)
  - 프로필 사진 (선택)
  - 이메일 (선택)
- [ ] `application.yml`에 Client ID, Secret 입력
```yaml
naver:
  api:
    key:
      client: "발급받은_CLIENT_ID"
      secret: "발급받은_CLIENT_SECRET"
```

### 2.3 Apple 로그인 설정 (선택사항, iOS 필수)
- [ ] [Apple Developer](https://developer.apple.com) 접속
- [ ] Apple Developer Program 가입 (연 $99)
- [ ] Certificates, Identifiers & Profiles 이동
- [ ] Identifiers 생성
  - [ ] App IDs 생성
    - Bundle ID: `com.perfacto.app`
    - Sign in with Apple 활성화
  - [ ] Services IDs 생성
    - Identifier: `com.perfacto.app.signin`
    - Return URLs: `https://api.perfacto.com/perfacto/auth/apple-login`
- [ ] Keys 생성
  - [ ] Sign in with Apple 키 생성
  - Key ID 복사
  - .p8 파일 다운로드
- [ ] Team ID 확인
  - Membership 페이지에서 확인
- [ ] 서버에 설정
  - [ ] .p8 파일을 `src/main/resources/keys/` 폴더에 복사
  - [ ] `application.yml`에 설정 입력
```yaml
apple:
  api:
    key:
      team-id: "YOUR_TEAM_ID"
      key-id: "YOUR_KEY_ID"
      client-id: "com.perfacto.app.signin"
      path: "/keys/AuthKey.p8"
```

### 2.4 통합 테스트
- [ ] 카카오 로그인 테스트
  - [ ] 웹 브라우저에서 인가 코드 받기
  - [ ] Postman으로 로그인 API 호출
  - [ ] Access Token 및 사용자 정보 확인
- [ ] 네이버 로그인 테스트 (선택)
- [ ] Apple 로그인 테스트 (선택, iOS에서만 가능)

**산출물**:
- ✅ 카카오 REST API 키
- ✅ 네이버 Client ID/Secret (선택)
- ✅ Apple 인증 파일 및 설정 (선택)
- ✅ 업데이트된 `application.yml`
- ✅ 소셜 로그인 테스트 성공

---

## 📱 Phase 3: Flutter 앱 연동 (3-5일)

### 3.1 프로젝트 구조 결정
- [ ] Firebase 사용 결정
  - [ ] **옵션 A**: Firebase 완전 제거 (Spring Boot만 사용)
  - [ ] **옵션 B**: Firebase 병행 사용 (푸시 알림, Analytics 등)
- [ ] API Base URL 설정
  - 개발: `http://localhost:8080`
  - 프로덕션: `https://api.perfacto.com`

### 3.2 Flutter 패키지 설정
- [ ] `pubspec.yaml` 업데이트
```yaml
dependencies:
  http: ^1.1.0  # REST API 호출
  flutter_secure_storage: ^9.0.0  # JWT 토큰 저장
  provider: ^6.1.1  # 상태 관리

  # Firebase 제거 시 삭제
  # firebase_core: ^2.x.x
  # cloud_firestore: ^4.x.x
  # firebase_auth: ^4.x.x
```
- [ ] `flutter pub get` 실행

### 3.3 API 클라이언트 구현
- [ ] `lib/services/api_service.dart` 생성
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
- [ ] API 응답 모델 생성 (`lib/models/api_response.dart`)
- [ ] 에러 핸들링 구현

### 3.4 인증 로직 변경
- [ ] JWT 토큰 저장소 구현 (`lib/services/auth_storage.dart`)
```dart
class AuthStorage {
  final FlutterSecureStorage _storage = FlutterSecureStorage();

  Future<void> saveToken(String accessToken, String refreshToken) async {
    await _storage.write(key: 'access_token', value: accessToken);
    await _storage.write(key: 'refresh_token', value: refreshToken);
  }

  Future<String?> getAccessToken() async {
    return await _storage.read(key: 'access_token');
  }
}
```
- [ ] 소셜 로그인 플로우 구현
  - [ ] 카카오 로그인 (`kakao_flutter_sdk` 패키지 사용)
  - [ ] 로그인 버튼 UI
  - [ ] 인가 코드 받기
  - [ ] 백엔드 API 호출
  - [ ] JWT 토큰 저장
- [ ] 자동 로그인 구현
- [ ] 토큰 갱신 로직 구현

### 3.5 데이터 모델 생성
- [ ] `lib/models/place.dart` - 장소 모델
```dart
class Place {
  final int id;
  final String name;
  final String description;
  final String district;
  final String address;
  final double latitude;
  final double longitude;
  final Category category;
  final int likeCount;
  final int bookmarkCount;
  final double averageRating;

  factory Place.fromJson(Map<String, dynamic> json) {
    return Place(
      id: json['id'],
      name: json['name'],
      // ...
    );
  }
}
```
- [ ] `lib/models/category.dart` - 카테고리 모델
- [ ] `lib/models/review.dart` - 리뷰 모델
- [ ] `lib/models/user.dart` - 사용자 모델

### 3.6 Provider 상태 관리 구현
- [ ] `lib/providers/auth_provider.dart` - 인증 상태
- [ ] `lib/providers/place_provider.dart` - 장소 상태
- [ ] `lib/providers/category_provider.dart` - 카테고리 상태
- [ ] `lib/providers/user_provider.dart` - 사용자 상태

### 3.7 API 서비스 구현
- [ ] `lib/services/place_service.dart`
  - [ ] `getPlaces()` - 장소 목록
  - [ ] `getPlaceById(id)` - 장소 상세
  - [ ] `getHotPlaces()` - HOT 장소
  - [ ] `getNearbyPlaces(lat, lon)` - 주변 장소
  - [ ] `searchPlaces(keyword)` - 장소 검색
  - [ ] `createPlace()` - 장소 등록
- [ ] `lib/services/like_service.dart`
  - [ ] `addLike(placeId)` - 좋아요 추가
  - [ ] `removeLike(placeId)` - 좋아요 취소
  - [ ] `checkLike(placeId)` - 좋아요 여부
- [ ] `lib/services/bookmark_service.dart`
- [ ] `lib/services/review_service.dart`
- [ ] `lib/services/upload_service.dart` - 이미지 업로드

### 3.8 UI 화면 연동
- [ ] 홈 화면
  - [ ] 카테고리 탭 구현
  - [ ] HOT 장소 표시
  - [ ] 지도에 마커 표시 (Google Maps)
- [ ] 장소 상세 화면
  - [ ] 장소 정보 표시
  - [ ] 좋아요/북마크 버튼
  - [ ] 리뷰 목록
  - [ ] 평균 평점 표시
- [ ] 장소 등록 화면
  - [ ] 카테고리 선택
  - [ ] 위치 선택 (Google Maps)
  - [ ] 이미지 업로드
- [ ] 리뷰 작성 화면
- [ ] 내 정보 화면
  - [ ] 포인트 표시
  - [ ] 내가 등록한 장소
  - [ ] 내가 작성한 리뷰
  - [ ] 좋아요한 장소
  - [ ] 북마크한 장소

### 3.9 이미지 업로드 구현
- [ ] `image_picker` 패키지 추가
- [ ] 카메라/갤러리에서 이미지 선택
- [ ] MultipartFile로 변환
- [ ] 서버 업로드 API 호출
- [ ] 이미지 URL 받아서 표시

### 3.10 테스트
- [ ] 로그인/로그아웃 테스트
- [ ] 장소 CRUD 테스트
- [ ] 좋아요/북마크 테스트
- [ ] 리뷰 작성 테스트
- [ ] 이미지 업로드 테스트
- [ ] 에러 처리 테스트 (네트워크 오류, 인증 실패 등)

**산출물**:
- ✅ Firebase 제거 또는 병행 사용 설정
- ✅ 완전히 작동하는 Flutter 앱
- ✅ Spring Boot API와 완벽 연동
- ✅ 소셜 로그인 통합

---

## ☁️ Phase 4: AWS 프로덕션 배포 (3-5일)

### 4.1 AWS 계정 설정
- [ ] AWS 계정 생성 (Free Tier)
  - [ ] 이메일 주소 입력
  - [ ] 신용카드 등록 (Free Tier에서도 필요, 과금 없음)
  - [ ] 신원 확인 (전화번호)
- [ ] IAM 사용자 생성
  - [ ] 루트 계정 대신 IAM 사용자로 작업
  - [ ] 관리자 권한 부여
  - [ ] Access Key 생성 및 저장
- [ ] MFA (Multi-Factor Authentication) 설정 (선택, 권장)
- [ ] 리전 선택: **서울 (ap-northeast-2)** 선택

### 4.2 RDS (MySQL) 설정
- [ ] RDS 콘솔 접속
- [ ] 데이터베이스 생성
  - [ ] 엔진: MySQL 8.0
  - [ ] 템플릿: **프리 티어**
  - [ ] DB 인스턴스 식별자: `perfacto-db`
  - [ ] 마스터 사용자 이름: `admin`
  - [ ] 마스터 암호 설정 (강력한 비밀번호)
  - [ ] DB 인스턴스 클래스: `db.t3.micro` (Free Tier)
  - [ ] 스토리지: 20GB (Free Tier 최대)
  - [ ] 퍼블릭 액세스: **예** (개발용, 나중에 보안 강화)
  - [ ] VPC 보안 그룹: 새로 생성
- [ ] 보안 그룹 설정
  - [ ] 인바운드 규칙 추가
    - 유형: MySQL/Aurora (3306)
    - 소스: 내 IP (개발 시) 또는 EC2 보안 그룹
- [ ] 엔드포인트 주소 복사
  - 예: `perfacto-db.xxxxx.ap-northeast-2.rds.amazonaws.com`
- [ ] 로컬에서 연결 테스트
```bash
mysql -h perfacto-db.xxxxx.ap-northeast-2.rds.amazonaws.com -u admin -p
```
- [ ] `perfacto` 데이터베이스 생성
```sql
CREATE DATABASE perfacto CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**예상 비용**: Free Tier (12개월 무료)
- 750시간/월 (24시간 x 31일)
- 20GB 스토리지

### 4.3 EC2 인스턴스 설정
- [ ] EC2 콘솔 접속
- [ ] 인스턴스 시작
  - [ ] AMI: Ubuntu Server 22.04 LTS (Free Tier)
  - [ ] 인스턴스 유형: **t2.micro** (Free Tier)
  - [ ] 키 페어 생성 및 다운로드
    - 이름: `perfacto-key`
    - 파일: `perfacto-key.pem` 안전하게 보관
  - [ ] 네트워크 설정
    - [ ] 퍼블릭 IP 자동 할당: 활성화
    - [ ] 보안 그룹 생성
      - SSH (22): 내 IP
      - HTTP (80): 0.0.0.0/0
      - HTTPS (443): 0.0.0.0/0
      - Custom (8080): 0.0.0.0/0 (나중에 제거)
  - [ ] 스토리지: 30GB (Free Tier 최대)
- [ ] 인스턴스 시작
- [ ] Elastic IP 할당 (선택사항, 고정 IP)
  - [ ] Elastic IP 주소 할당
  - [ ] 인스턴스에 연결

**예상 비용**: Free Tier (12개월 무료)
- 750시간/월

### 4.4 EC2 서버 환경 설정
- [ ] SSH 접속
```bash
chmod 400 perfacto-key.pem
ssh -i perfacto-key.pem ubuntu@[EC2_PUBLIC_IP]
```
- [ ] 시스템 업데이트
```bash
sudo apt update
sudo apt upgrade -y
```
- [ ] JDK 17 설치
```bash
sudo apt install openjdk-17-jdk -y
java -version
```
- [ ] Nginx 설치 (리버스 프록시)
```bash
sudo apt install nginx -y
sudo systemctl start nginx
sudo systemctl enable nginx
```
- [ ] 작업 디렉토리 생성
```bash
mkdir ~/perfacto
cd ~/perfacto
```

### 4.5 애플리케이션 배포
- [ ] 로컬에서 JAR 빌드
```bash
cd /Users/mac/spring_boot_proj/perfacto_server
./gradlew clean bootJar
```
- [ ] JAR 파일을 EC2로 전송
```bash
scp -i perfacto-key.pem build/libs/app.jar ubuntu@[EC2_PUBLIC_IP]:~/perfacto/
```
- [ ] 프로덕션 설정 파일 생성
```bash
ssh -i perfacto-key.pem ubuntu@[EC2_PUBLIC_IP]
cd ~/perfacto
nano application-prod.yml
```
```yaml
spring:
  datasource:
    url: jdbc:mysql://perfacto-db.xxxxx.ap-northeast-2.rds.amazonaws.com:3306/perfacto
    username: admin
    password: YOUR_RDS_PASSWORD
  jpa:
    hibernate:
      ddl-auto: validate
```
- [ ] 환경 변수 설정
```bash
nano ~/.bashrc
# 맨 아래 추가
export JWT_SECRET="your-production-secret-key"
export DB_PASSWORD="your-rds-password"
source ~/.bashrc
```
- [ ] 서버 실행 테스트
```bash
java -jar -Dspring.profiles.active=prod app.jar
```
- [ ] 브라우저에서 확인
```
http://[EC2_PUBLIC_IP]:8080/perfacto/every/categories
```

### 4.6 Systemd 서비스 등록 (자동 재시작)
- [ ] 서비스 파일 생성
```bash
sudo nano /etc/systemd/system/perfacto.service
```
```ini
[Unit]
Description=Perfacto Spring Boot Application
After=syslog.target network.target

[Service]
User=ubuntu
WorkingDirectory=/home/ubuntu/perfacto
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod /home/ubuntu/perfacto/app.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

Environment="JWT_SECRET=your-production-secret-key"
Environment="DB_PASSWORD=your-rds-password"

[Install]
WantedBy=multi-user.target
```
- [ ] 서비스 시작
```bash
sudo systemctl daemon-reload
sudo systemctl start perfacto
sudo systemctl enable perfacto
sudo systemctl status perfacto
```
- [ ] 로그 확인
```bash
sudo journalctl -u perfacto -f
```

### 4.7 Nginx 리버스 프록시 설정
- [ ] Nginx 설정 파일 생성
```bash
sudo nano /etc/nginx/sites-available/perfacto
```
```nginx
server {
    listen 80;
    server_name [EC2_PUBLIC_IP_OR_DOMAIN];

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```
- [ ] 심볼릭 링크 생성
```bash
sudo ln -s /etc/nginx/sites-available/perfacto /etc/nginx/sites-enabled/
sudo rm /etc/nginx/sites-enabled/default
```
- [ ] Nginx 재시작
```bash
sudo nginx -t
sudo systemctl restart nginx
```
- [ ] 브라우저에서 확인
```
http://[EC2_PUBLIC_IP]/perfacto/every/categories
```

### 4.8 도메인 및 HTTPS 설정 (선택사항)
- [ ] 도메인 구매 (Gabia, Route53 등)
  - 예: `perfacto.com`
- [ ] Route 53 (AWS DNS)
  - [ ] 호스팅 영역 생성
  - [ ] A 레코드 추가
    - 이름: `api.perfacto.com`
    - 값: EC2 Elastic IP
- [ ] 도메인 네임서버 변경
  - Route 53의 NS 레코드를 도메인 등록기관에 입력
- [ ] SSL 인증서 발급 (Let's Encrypt)
```bash
sudo apt install certbot python3-certbot-nginx -y
sudo certbot --nginx -d api.perfacto.com
```
- [ ] 자동 갱신 설정
```bash
sudo certbot renew --dry-run
```
- [ ] HTTPS 확인
```
https://api.perfacto.com/perfacto/every/categories
```

### 4.9 보안 강화
- [ ] SSH 포트 변경 (선택)
- [ ] 방화벽 설정 (UFW)
```bash
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw enable
```
- [ ] RDS 보안 그룹 업데이트
  - EC2 보안 그룹만 허용
- [ ] EC2 보안 그룹 업데이트
  - 8080 포트 제거 (Nginx를 통해서만 접근)
- [ ] 환경 변수 암호화 (AWS Secrets Manager 또는 Parameter Store)

### 4.10 모니터링 및 로깅
- [ ] CloudWatch 설정
  - [ ] EC2 메트릭 확인 (CPU, 메모리)
  - [ ] RDS 메트릭 확인
  - [ ] 알람 설정 (CPU > 80%)
- [ ] 로그 수집
  - [ ] CloudWatch Logs Agent 설치 (선택)
  - [ ] 애플리케이션 로그 전송
- [ ] 백업 설정
  - [ ] RDS 자동 백업 활성화 (7일 보관)
  - [ ] EC2 AMI 생성 (주기적)

### 4.11 배포 자동화 (선택사항)
- [ ] GitHub Actions CI/CD 설정
```yaml
# .github/workflows/deploy.yml
name: Deploy to AWS

on:
  push:
    branches: [ main ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Build with Gradle
        run: ./gradlew bootJar
      - name: Deploy to EC2
        uses: appleboy/scp-action@master
        with:
          host: ${{ secrets.EC2_HOST }}
          username: ubuntu
          key: ${{ secrets.EC2_KEY }}
          source: "build/libs/app.jar"
          target: "/home/ubuntu/perfacto"
      - name: Restart application
        uses: appleboy/ssh-action@master
        with:
          host: ${{ secrets.EC2_HOST }}
          username: ubuntu
          key: ${{ secrets.EC2_KEY }}
          script: sudo systemctl restart perfacto
```
- [ ] GitHub Secrets 설정
  - EC2_HOST
  - EC2_KEY

### 4.12 Flutter 앱 설정 업데이트
- [ ] API Base URL 변경
```dart
class ApiConfig {
  static const String baseUrl = 'https://api.perfacto.com';
  // 또는
  static const String baseUrl = 'http://[EC2_PUBLIC_IP]';
}
```
- [ ] 소셜 로그인 Redirect URI 업데이트
  - 카카오: `https://api.perfacto.com/perfacto/auth/kakao-login`
  - 네이버: `https://api.perfacto.com/perfacto/auth/naver-login`
  - Apple: `https://api.perfacto.com/perfacto/auth/apple-login`
- [ ] 앱 빌드 및 테스트
- [ ] 실제 디바이스에서 프로덕션 API 테스트

### 4.13 법인카드 연결 준비
- [ ] AWS 비용 확인
  - [ ] Cost Explorer 활성화
  - [ ] 예산 알람 설정 (월 $10, $50 등)
- [ ] Free Tier 사용량 모니터링
  - [ ] EC2: 750시간/월
  - [ ] RDS: 750시간/월
  - [ ] 스토리지: 30GB
- [ ] 법인카드 등록 시점 결정
  - Free Tier 만료 전 (12개월)
  - 또는 사용량 초과 예상 시
- [ ] 법인카드 등록
  - [ ] AWS 계정 설정 → 결제 정보
  - [ ] 법인카드 정보 입력
  - [ ] 청구서 수신 이메일 설정

**산출물**:
- ✅ AWS에서 실행되는 프로덕션 서버
- ✅ RDS MySQL 데이터베이스
- ✅ 도메인 및 HTTPS 설정 (선택)
- ✅ 자동 재시작 및 모니터링
- ✅ Flutter 앱 프로덕션 연동

---

## 📊 전체 일정 요약

| Phase | 작업 | 예상 기간 | 의존성 |
|-------|------|-----------|--------|
| 1 | 서버 실행 및 테스트 | 1-2일 | 없음 |
| 2 | 소셜 로그인 설정 | 1-2일 | Phase 1 |
| 3 | Flutter 앱 연동 | 3-5일 | Phase 1, 2 |
| 4 | AWS 프로덕션 배포 | 3-5일 | Phase 1, 2 |

**총 예상 기간**: **2-3주**

---

## 💰 예상 비용 (AWS Free Tier)

### Free Tier (12개월)
- **EC2 t2.micro**: 750시간/월 (무료)
- **RDS db.t3.micro**: 750시간/월 (무료)
- **스토리지**: 30GB (무료)
- **데이터 전송**: 15GB 아웃바운드 (무료)

### Free Tier 초과 시 (법인카드 연결 후)
- **EC2 t2.micro**: 약 $8.5/월
- **RDS db.t3.micro**: 약 $15/월
- **스토리지**: 20GB SSD - $2.3/월
- **예상 총액**: **약 $25-30/월**

### 선택 사항
- **도메인**: $10-15/년
- **Elastic IP (사용 중)**: 무료
- **Elastic IP (미사용)**: $0.005/시간
- **Route 53**: $0.5/호스팅 영역/월

---

## ✅ 체크리스트 요약

### Phase 1: 서버 실행 (필수)
- [ ] MySQL 데이터베이스 생성
- [ ] application.yml 설정
- [ ] 서버 실행 성공
- [ ] API 테스트 완료

### Phase 2: 소셜 로그인 (필수: 카카오)
- [ ] 카카오 개발자 등록 및 키 발급
- [ ] 네이버 개발자 등록 (선택)
- [ ] Apple 개발자 등록 (선택, iOS 필수)
- [ ] 로그인 테스트 성공

### Phase 3: Flutter 연동 (필수)
- [ ] API 클라이언트 구현
- [ ] 인증 로직 구현
- [ ] UI 화면 연동
- [ ] 통합 테스트 완료

### Phase 4: AWS 배포 (필수)
- [ ] RDS MySQL 생성
- [ ] EC2 인스턴스 생성
- [ ] 애플리케이션 배포
- [ ] Nginx 설정
- [ ] HTTPS 설정 (선택)
- [ ] Flutter 앱 프로덕션 테스트

---

## 📝 참고 자료

### 공식 문서
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [AWS Free Tier](https://aws.amazon.com/free/)
- [Kakao Developers](https://developers.kakao.com)
- [Flutter Documentation](https://flutter.dev/docs)

### 내부 문서
- `PERFACTO_API_GUIDE.md` - 전체 API 문서
- `SETUP_GUIDE.md` - 로컬 설정 가이드
- `SOCIAL_LOGIN_GUIDE.md` - 소셜 로그인 가이드

### 도구
- [Postman](https://www.postman.com/) - API 테스트
- [DBeaver](https://dbeaver.io/) - DB 관리
- [VSCode](https://code.visualstudio.com/) - 코드 에디터

---

## 🎯 성공 기준

- ✅ 로컬에서 백엔드 서버 정상 실행
- ✅ 카카오 로그인 성공
- ✅ Flutter 앱에서 장소 CRUD 성공
- ✅ AWS에서 프로덕션 서버 실행
- ✅ 실제 디바이스에서 앱 테스트 성공

---

**이 WBS를 Notion에 복사하여 사용하세요!**
각 체크박스를 완료하면서 진행 상황을 추적할 수 있습니다.
