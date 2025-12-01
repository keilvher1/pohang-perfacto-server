# Perfacto Server 설정 가이드

Perfacto 백엔드 서버를 처음 설정하고 실행하는 방법을 안내합니다.

## 1. 사전 준비

### 필수 설치 항목
- [x] JDK 17 이상
- [x] MySQL 8.0
- [x] Gradle 8.10.2 (프로젝트에 포함됨)
- [ ] Redis (선택사항)

### 설치 확인
```bash
java -version  # Java 17 이상 확인
mysql --version  # MySQL 8.0 이상 확인
```

## 2. 데이터베이스 설정

### MySQL 데이터베이스 생성
```bash
mysql -u root -p
```

```sql
-- 데이터베이스 생성
CREATE DATABASE perfacto CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 사용자 생성 및 권한 부여 (선택사항)
CREATE USER 'perfacto_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON perfacto.* TO 'perfacto_user'@'localhost';
FLUSH PRIVILEGES;

-- 생성 확인
SHOW DATABASES;
USE perfacto;
```

## 3. 프로젝트 설정

### application.yml 수정

`src/main/resources/application.yml` 파일을 열고 다음 부분을 수정하세요:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/perfacto
    username: perfacto_user  # 사용자 이름
    password: your_password  # 비밀번호
```

### JPA DDL 설정 변경

데이터베이스 테이블을 자동 생성하려면:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create  # 최초 실행 시에만 create, 이후 validate로 변경
```

**주의**:
- `create`: 기존 테이블 삭제 후 재생성 (데이터 손실)
- `update`: 테이블 스키마 업데이트
- `validate`: 스키마 검증만 수행
- `none`: 아무 작업도 하지 않음

### 카카오 로그인 설정 (선택사항)

카카오 로그인을 사용하려면:

1. [Kakao Developers](https://developers.kakao.com/)에서 애플리케이션 등록
2. REST API 키 발급
3. `application.yml` 수정:

```yaml
kakao:
  api:
    key:
      client: "발급받은_REST_API_KEY"
    redirect-uri: "http://localhost:8080/perfacto/auth/kakao-login"
```

## 4. 빌드 및 실행

### 방법 1: Gradle로 직접 실행
```bash
# 프로젝트 디렉토리로 이동
cd /Users/mac/spring_boot_proj/perfacto_server

# 의존성 다운로드
./gradlew clean build

# 서버 실행
./gradlew bootRun
```

### 방법 2: JAR 파일로 실행
```bash
# JAR 빌드
./gradlew bootJar

# 실행
java -jar build/libs/app.jar
```

### 실행 확인
서버가 정상적으로 실행되면 다음과 같은 로그가 출력됩니다:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

...
Started ScrdApplication in X.XXX seconds
```

브라우저에서 확인:
```
http://localhost:8080
```

## 5. API 테스트

### 카테고리 조회 (인증 불필요)
```bash
curl http://localhost:8080/perfacto/every/categories
```

예상 응답:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "code": "restaurant",
      "name": "음식점",
      "nameEn": "Restaurant"
    },
    ...
  ]
}
```

### Postman 컬렉션

Postman을 사용하는 경우:
1. `PERFACTO_API_GUIDE.md`의 API 문서 참조
2. 각 엔드포인트를 Postman에 추가
3. Authorization 탭에서 Bearer Token 설정

## 6. 초기 데이터 설정

서버가 처음 실행되면 다음 데이터가 자동으로 생성됩니다:

### 기본 카테고리
- **restaurant** (음식점)
- **accommodation** (숙박)
- **cafe** (카페)
- **attraction** (관광지)

확인:
```bash
curl http://localhost:8080/perfacto/every/categories
```

## 7. 파일 업로드 디렉토리 설정

### 업로드 디렉토리 생성
```bash
# 프로젝트 루트에서
mkdir -p /uploads
chmod 755 /uploads
```

또는 `application.yml`에서 경로 변경:
```yaml
file:
  upload:
    path: /Users/mac/uploads  # 원하는 경로로 변경
```

## 8. 포트 변경 (선택사항)

기본 포트 8080을 변경하려면:

```yaml
server:
  port: 9090  # 원하는 포트 번호
```

## 9. 로그 설정

### 로그 레벨 조정

```yaml
logging:
  level:
    root: INFO
    org.example.scrd: DEBUG
    org.hibernate.SQL: DEBUG
```

### 로그 파일 저장

```yaml
logging:
  file:
    name: logs/perfacto.log
    max-size: 10MB
    max-history: 30
```

## 10. 프로덕션 배포

### 1. 프로덕션용 프로파일 생성

`src/main/resources/application-prod.yml` 생성:

```yaml
spring:
  datasource:
    url: jdbc:mysql://your-prod-db:3306/perfacto
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate  # 프로덕션에서는 validate 사용
    show-sql: false

logging:
  level:
    root: WARN
    org.example.scrd: INFO
```

### 2. 환경 변수 설정

```bash
export DB_USERNAME=perfacto_user
export DB_PASSWORD=secure_password
export JWT_SECRET=your-secure-jwt-secret-key
```

### 3. 프로덕션 모드로 실행

```bash
java -jar -Dspring.profiles.active=prod build/libs/app.jar
```

## 11. 문제 해결

### MySQL 연결 실패
```
Error: Communications link failure
```

**해결방법**:
1. MySQL 서버가 실행 중인지 확인
```bash
# macOS
brew services list | grep mysql

# Linux
sudo systemctl status mysql
```

2. 방화벽 확인
3. `application.yml`의 URL, username, password 확인

### 포트 충돌
```
Error: Port 8080 is already in use
```

**해결방법**:
1. 다른 포트 사용
2. 기존 프로세스 종료
```bash
lsof -ti:8080 | xargs kill -9
```

### JPA 테이블 생성 실패

**해결방법**:
1. `ddl-auto`를 `create`로 변경
2. 수동으로 SQL 스크립트 실행

## 12. 개발 팁

### Hot Reload 설정 (Spring DevTools)

`build.gradle`에 추가:
```gradle
dependencies {
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
}
```

### H2 Console (개발용 DB)

개발 시 H2 인메모리 DB 사용:

```yaml
spring:
  h2:
    console:
      enabled: true
  datasource:
    url: jdbc:h2:mem:testdb
```

접속: `http://localhost:8080/h2-console`

## 13. 다음 단계

1. `PERFACTO_API_GUIDE.md`에서 전체 API 문서 확인
2. Flutter 앱과 연동
3. 장소 데이터 등록 테스트
4. 이미지 업로드 테스트

## 지원

문제가 발생하면:
1. 로그 확인: `logs/perfacto.log`
2. GitHub Issues 등록
3. `PERFACTO_API_GUIDE.md` 참조
