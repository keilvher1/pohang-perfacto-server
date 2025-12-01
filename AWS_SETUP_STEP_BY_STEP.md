# AWS Free Tier 설정 가이드 - 단계별 상세 안내

## 📋 목차
1. [AWS 계정 생성](#1-aws-계정-생성)
2. [RDS MySQL 데이터베이스 설정](#2-rds-mysql-데이터베이스-설정)
3. [EC2 인스턴스 생성](#3-ec2-인스턴스-생성)
4. [EC2 환경 설정](#4-ec2-환경-설정)
5. [Spring Boot 애플리케이션 배포](#5-spring-boot-애플리케이션-배포)
6. [Nginx 및 보안 설정](#6-nginx-및-보안-설정)
7. [테스트 및 검증](#7-테스트-및-검증)

---

## 1. AWS 계정 생성

### 1.1 AWS 계정 만들기

1. **AWS 홈페이지 접속**
   - https://aws.amazon.com/ko/
   - 우측 상단 "AWS 계정 생성" 클릭

2. **이메일 및 계정 정보 입력**
   ```
   이메일 주소: your-email@example.com
   AWS 계정 이름: perfacto-production
   ```

3. **루트 사용자 암호 설정**
   - 강력한 비밀번호 사용 (대소문자, 숫자, 특수문자 포함)
   - 안전한 곳에 저장

4. **연락처 정보 입력**
   - 개인 또는 비즈니스 선택
   - 전화번호, 주소 입력

5. **결제 정보 입력**
   - 신용카드 또는 체크카드 등록
   - **주의**: Free Tier 사용 시에도 카드 등록 필요
   - **Free Tier 범위 내에서는 과금되지 않음**
   - $1 승인 테스트 후 자동 취소됨

6. **신원 확인**
   - 전화번호 인증
   - 자동 음성 또는 SMS로 인증 코드 수신
   - 4자리 코드 입력

7. **지원 플랜 선택**
   - **"기본 지원 - 무료" 선택**
   - 유료 플랜은 필요 없음

8. **계정 생성 완료**
   - 환영 이메일 수신 확인
   - 몇 분 후 계정 활성화됨

### 1.2 IAM 사용자 생성 (보안 강화)

1. **AWS Management Console 로그인**
   - https://console.aws.amazon.com
   - 루트 계정으로 로그인

2. **IAM 서비스 검색**
   - 상단 검색창에 "IAM" 입력
   - IAM 서비스 클릭

3. **사용자 추가**
   - 좌측 메뉴 "사용자" 클릭
   - "사용자 추가" 버튼 클릭
   - 사용자 이름: `perfacto-admin`
   - AWS 액세스 유형: "AWS Management Console 액세스" 체크
   - 콘솔 비밀번호: "사용자 지정 비밀번호" 선택
   - 비밀번호 재설정 필요: 체크 해제

4. **권한 설정**
   - "기존 정책 직접 연결" 선택
   - `AdministratorAccess` 검색 및 체크
   - "다음: 태그" 클릭
   - "다음: 검토" 클릭
   - "사용자 만들기" 클릭

5. **로그인 정보 저장**
   ```
   콘솔 로그인 링크: https://YOUR_ACCOUNT_ID.signin.aws.amazon.com/console
   사용자 이름: perfacto-admin
   비밀번호: (설정한 비밀번호)
   ```
   - **이제부터 이 IAM 사용자로 로그인!**

### 1.3 리전 선택

1. **콘솔 우측 상단 리전 확인**
   - 현재 리전 표시됨 (예: 버지니아 북부)

2. **서울 리전으로 변경**
   - 리전 드롭다운 클릭
   - **"아시아 태평양(서울) ap-northeast-2"** 선택
   - ⚠️ **모든 작업을 서울 리전에서 수행!**

### 1.4 비용 알림 설정

1. **결제 대시보드 이동**
   - 우측 상단 계정명 클릭
   - "결제 및 비용 관리" 클릭

2. **예산 생성**
   - 좌측 메뉴 "예산" 클릭
   - "예산 생성" 클릭
   - 템플릿: "비용 예산"
   - 예산 이름: `perfacto-monthly-budget`
   - 예산 금액: `$10` (알림용)
   - 이메일 주소 입력
   - "예산 생성" 클릭

---

## 2. RDS MySQL 데이터베이스 설정

### 2.1 RDS 콘솔 접속

1. **RDS 서비스 검색**
   - 상단 검색창에 "RDS" 입력
   - "RDS" 클릭

2. **리전 확인**
   - 우측 상단이 "서울" 리전인지 재확인

### 2.2 데이터베이스 생성

1. **데이터베이스 생성 시작**
   - "데이터베이스 생성" 버튼 클릭

2. **엔진 옵션**
   ```
   엔진 유형: MySQL
   버전: MySQL 8.0.35 (최신 8.0 버전)
   ```

3. **템플릿**
   ```
   ⚠️ 중요: "프리 티어" 선택!
   ```

4. **설정**
   ```
   DB 인스턴스 식별자: perfacto-db
   마스터 사용자 이름: admin
   마스터 암호: (강력한 비밀번호 설정)
   암호 확인: (동일하게 재입력)
   ```
   **비밀번호 예시**: `Perfacto2025!@#`
   - 안전한 곳에 저장하세요!

5. **DB 인스턴스 크기**
   ```
   DB 인스턴스 클래스: db.t3.micro (프리 티어)
   스토리지 유형: 범용 SSD (gp2)
   할당된 스토리지: 20GB (프리 티어 최대)
   스토리지 자동 조정: 활성화 체크 해제
   ```

6. **연결**
   ```
   Virtual Private Cloud (VPC): 기본 VPC
   퍼블릭 액세스: 예 ✅ (중요: 개발 단계에서 필요)
   VPC 보안 그룹: 새로 생성
   보안 그룹 이름: perfacto-db-sg
   가용 영역: 기본값
   ```

7. **추가 구성 (확장)**
   ```
   초기 데이터베이스 이름: perfacto
   DB 파라미터 그룹: 기본값
   백업 보존 기간: 7일
   암호화: 비활성화 (프리 티어)
   성능 개선 도구: 비활성화 (비용 절감)
   모니터링: 기본 모니터링
   ```

8. **생성 완료**
   - "데이터베이스 생성" 버튼 클릭
   - **생성 완료까지 약 5-10분 소요**
   - 상태가 "사용 가능"이 될 때까지 대기

### 2.3 보안 그룹 설정

1. **생성된 DB 클릭**
   - 상태가 "사용 가능"인 `perfacto-db` 클릭

2. **연결 & 보안 탭**
   - "VPC 보안 그룹" 클릭 (파란색 링크)

3. **인바운드 규칙 편집**
   - "인바운드 규칙" 탭 클릭
   - "인바운드 규칙 편집" 버튼 클릭
   - "규칙 추가" 클릭
   ```
   유형: MySQL/Aurora
   프로토콜: TCP
   포트 범위: 3306
   소스: 내 IP (자동으로 현재 IP 입력됨)
   설명: My development IP
   ```
   - "규칙 저장" 클릭

### 2.4 엔드포인트 확인 및 연결 테스트

1. **엔드포인트 주소 복사**
   - RDS 콘솔로 돌아가기
   - `perfacto-db` 클릭
   - "연결 & 보안" 섹션
   - 엔드포인트 복사 (예: `perfacto-db.xxxxxxxxx.ap-northeast-2.rds.amazonaws.com`)

2. **로컬에서 연결 테스트**
   ```bash
   # MySQL 클라이언트로 연결
   mysql -h perfacto-db.xxxxxxxxx.ap-northeast-2.rds.amazonaws.com -P 3306 -u admin -p

   # 비밀번호 입력
   # 성공하면 mysql> 프롬프트 표시됨
   ```

3. **데이터베이스 확인**
   ```sql
   SHOW DATABASES;
   -- perfacto 데이터베이스가 있어야 함

   USE perfacto;

   -- 테이블 확인 (아직 비어있음)
   SHOW TABLES;

   -- 연결 종료
   EXIT;
   ```

4. **연결 실패 시**
   - 보안 그룹 규칙 재확인
   - 내 IP가 올바른지 확인
   - 엔드포인트 주소가 정확한지 확인
   - 비밀번호가 정확한지 확인

---

## 3. EC2 인스턴스 생성

### 3.1 EC2 콘솔 접속

1. **EC2 서비스 검색**
   - 상단 검색창에 "EC2" 입력
   - "EC2" 클릭

2. **인스턴스 시작**
   - "인스턴스 시작" 버튼 클릭

### 3.2 인스턴스 설정

1. **이름 및 태그**
   ```
   이름: perfacto-server
   ```

2. **애플리케이션 및 OS 이미지 (AMI)**
   ```
   빠른 시작: Ubuntu
   Amazon Machine Image (AMI): Ubuntu Server 22.04 LTS (HVM), SSD Volume Type
   아키텍처: 64비트 (x86)
   ⚠️ "프리 티어 사용 가능" 표시 확인!
   ```

3. **인스턴스 유형**
   ```
   인스턴스 유형: t2.micro
   ⚠️ "프리 티어 사용 가능" 표시 확인!
   ```

4. **키 페어 (로그인)**
   - "새 키 페어 생성" 클릭
   ```
   키 페어 이름: perfacto-key
   키 페어 유형: RSA
   프라이빗 키 파일 형식: .pem (Mac/Linux용)
   ```
   - "키 페어 생성" 클릭
   - **`perfacto-key.pem` 파일 자동 다운로드**
   - ⚠️ **이 파일을 안전한 곳에 보관! 재다운로드 불가!**

   ```bash
   # 다운로드된 키를 안전한 위치로 이동
   mv ~/Downloads/perfacto-key.pem ~/.ssh/
   chmod 400 ~/.ssh/perfacto-key.pem
   ```

5. **네트워크 설정**
   - "편집" 클릭
   ```
   VPC: 기본 VPC
   서브넷: 기본값 (us-east-2a 등)
   퍼블릭 IP 자동 할당: 활성화 ✅
   ```

6. **방화벽 (보안 그룹)**
   - "보안 그룹 생성" 선택
   ```
   보안 그룹 이름: perfacto-server-sg
   설명: Security group for Perfacto server
   ```

   - **인바운드 보안 그룹 규칙**:

   **규칙 1: SSH**
   ```
   유형: SSH
   프로토콜: TCP
   포트 범위: 22
   소스 유형: 내 IP
   설명: SSH from my IP
   ```

   **규칙 2: HTTP**
   - "보안 그룹 규칙 추가" 클릭
   ```
   유형: HTTP
   프로토콜: TCP
   포트 범위: 80
   소스 유형: 위치 무관 (0.0.0.0/0)
   설명: HTTP from anywhere
   ```

   **규칙 3: HTTPS**
   - "보안 그룹 규칙 추가" 클릭
   ```
   유형: HTTPS
   프로토콜: TCP
   포트 범위: 443
   소스 유형: 위치 무관 (0.0.0.0/0)
   설명: HTTPS from anywhere
   ```

   **규칙 4: Custom (Spring Boot - 임시)**
   - "보안 그룹 규칙 추가" 클릭
   ```
   유형: 사용자 지정 TCP
   프로토콜: TCP
   포트 범위: 8080
   소스 유형: 위치 무관 (0.0.0.0/0)
   설명: Spring Boot (temporary)
   ```

7. **스토리지 구성**
   ```
   볼륨 1 (루트)
   크기: 30 GiB (프리 티어 최대)
   볼륨 유형: 범용 SSD (gp3)
   IOPS: 3000
   처리량: 125 MB/s
   종료 시 삭제: 예 (기본값)
   ```

8. **고급 세부 정보 (선택사항, 펼치기)**
   - 기본값 유지

### 3.3 인스턴스 시작

1. **요약 확인**
   - 우측 "요약" 패널에서 모든 설정 확인
   - 프리 티어 사용 가능 여부 확인

2. **인스턴스 시작**
   - "인스턴스 시작" 버튼 클릭
   - 성공 메시지 확인
   - "모든 인스턴스 보기" 클릭

3. **인스턴스 상태 확인**
   - 인스턴스 상태: "실행 중"이 될 때까지 대기 (약 1-2분)
   - 상태 검사: "2/2 검사 통과"가 될 때까지 대기 (약 2-3분)

### 3.4 Elastic IP 할당 (선택사항, 권장)

고정 IP를 원하는 경우:

1. **Elastic IP 메뉴**
   - 좌측 메뉴 "네트워크 및 보안" → "탄력적 IP" 클릭

2. **Elastic IP 주소 할당**
   - "탄력적 IP 주소 할당" 버튼 클릭
   - "할당" 버튼 클릭

3. **인스턴스에 연결**
   - 할당된 IP 선택
   - "작업" → "탄력적 IP 주소 연결" 클릭
   - 인스턴스: `perfacto-server` 선택
   - "연결" 클릭

⚠️ **주의**: Elastic IP는 인스턴스에 연결된 상태에서는 무료이지만, 연결 해제 시 시간당 과금됩니다!

### 3.5 SSH 연결 테스트

1. **퍼블릭 IP 확인**
   - 인스턴스 선택
   - "퍼블릭 IPv4 주소" 복사 (예: 13.125.XXX.XXX)

2. **SSH 연결**
   ```bash
   ssh -i ~/.ssh/perfacto-key.pem ubuntu@13.125.XXX.XXX

   # 처음 연결 시 fingerprint 확인
   # "yes" 입력

   # 성공하면 Ubuntu 환영 메시지 표시됨
   ```

3. **연결 확인**
   ```bash
   # 시스템 정보 확인
   uname -a
   # Ubuntu 22.04 표시되어야 함

   # 연결 종료 (나중에 다시 사용)
   exit
   ```

---

## 4. EC2 환경 설정

SSH로 EC2에 접속한 상태에서 진행:

```bash
ssh -i ~/.ssh/perfacto-key.pem ubuntu@13.125.XXX.XXX
```

### 4.1 시스템 업데이트

```bash
# 패키지 목록 업데이트
sudo apt update

# 설치된 패키지 업그레이드
sudo apt upgrade -y

# 재부팅 필요 시 (선택사항)
# sudo reboot
# 1분 후 다시 SSH 접속
```

### 4.2 JDK 17 설치

```bash
# OpenJDK 17 설치
sudo apt install openjdk-17-jdk -y

# 설치 확인
java -version
# openjdk version "17.x.x" 표시되어야 함

javac -version
# javac 17.x.x 표시되어야 함

# JAVA_HOME 환경 변수 설정
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$PATH:$JAVA_HOME/bin' >> ~/.bashrc
source ~/.bashrc

# 확인
echo $JAVA_HOME
```

### 4.3 Nginx 설치

```bash
# Nginx 설치
sudo apt install nginx -y

# Nginx 시작
sudo systemctl start nginx

# 부팅 시 자동 시작 설정
sudo systemctl enable nginx

# 상태 확인
sudo systemctl status nginx
# "active (running)" 표시되어야 함

# 웹 브라우저에서 확인
# http://13.125.XXX.XXX
# Nginx 기본 페이지가 표시되어야 함
```

### 4.4 MySQL 클라이언트 설치

```bash
# MySQL 클라이언트 설치
sudo apt install mysql-client -y

# RDS 연결 테스트
mysql -h perfacto-db.xxxxxxxxx.ap-northeast-2.rds.amazonaws.com -u admin -p

# 비밀번호 입력
# 성공하면 mysql> 프롬프트 표시

# 데이터베이스 확인
SHOW DATABASES;

# 종료
EXIT;
```

### 4.5 작업 디렉토리 생성

```bash
# 애플리케이션 디렉토리 생성
mkdir -p ~/perfacto
cd ~/perfacto

# 로그 디렉토리 생성
sudo mkdir -p /var/log/perfacto
sudo chown ubuntu:ubuntu /var/log/perfacto

# 확인
pwd
# /home/ubuntu/perfacto 표시되어야 함
```

---

## 5. Spring Boot 애플리케이션 배포

### 5.1 로컬에서 JAR 파일 빌드

**로컬 Mac에서 실행** (새 터미널 창):

```bash
# 프로젝트 디렉토리로 이동
cd /Users/mac/spring_boot_proj/perfacto_server

# 이전 빌드 정리
./gradlew clean

# JAR 빌드
./gradlew bootJar

# 빌드 확인
ls -lh build/libs/app.jar
# 파일 크기 표시되어야 함 (약 50-100MB)
```

### 5.2 프로덕션 설정 파일 생성

**로컬에서**:

```bash
cd /Users/mac/spring_boot_proj/perfacto_server
nano application-prod.yml
```

다음 내용 입력:

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://perfacto-db.xxxxxxxxx.ap-northeast-2.rds.amazonaws.com:3306/perfacto
    username: admin
    password: YOUR_RDS_PASSWORD_HERE
    hikari:
      maximum-pool-size: 5
      idle-timeout: 600000
      max-lifetime: 600000
      connection-timeout: 30000

  jpa:
    hibernate:
      ddl-auto: update  # 최초 배포 시 update, 이후 validate
    properties:
      hibernate:
        format_sql: false
    show-sql: false

  data:
    web:
      pageable:
        max-page-size: 2000

logging:
  level:
    root: INFO
    org.example.scrd: INFO
  file:
    name: /var/log/perfacto/application.log
    max-size: 10MB
    max-history: 30

custom:
  host:
    client: http://13.125.XXX.XXX,https://perfacto-7aa56.web.app,https://your-domain.com
  jwt:
    secret: "${JWT_SECRET:gjITbPwjXp3ZvUfcGx+sxaeB3Mdxcbfur9ZGZkmwgQs=}"
    expire-time-ms: 7200000000
    refresh-expire-time-ms: 7200000000

kakao:
  api:
    key:
      client: "6b4d8cc48ec73499504d519e26c84c91"
    auth-url: "https://kauth.kakao.com/oauth/authorize"
    redirect-uri: "http://13.125.XXX.XXX/perfacto/auth/kakao-login"

file:
  upload:
    path: /home/ubuntu/perfacto/uploads
    url-prefix: /files
```

**⚠️ 수정 필요 항목**:
1. RDS 엔드포인트 주소
2. RDS 비밀번호
3. EC2 퍼블릭 IP (13.125.XXX.XXX)

저장: `Ctrl+O`, Enter, `Ctrl+X`

### 5.3 파일을 EC2로 전송

```bash
# JAR 파일 전송
scp -i ~/.ssh/perfacto-key.pem \
  build/libs/app.jar \
  ubuntu@13.125.XXX.XXX:~/perfacto/

# 설정 파일 전송
scp -i ~/.ssh/perfacto-key.pem \
  application-prod.yml \
  ubuntu@13.125.XXX.XXX:~/perfacto/

# 전송 확인 (EC2에서)
ssh -i ~/.ssh/perfacto-key.pem ubuntu@13.125.XXX.XXX
cd ~/perfacto
ls -lh
# app.jar, application-prod.yml 확인
```

### 5.4 업로드 디렉토리 생성

**EC2에서**:

```bash
# 업로드 디렉토리 생성
mkdir -p ~/perfacto/uploads
chmod 755 ~/perfacto/uploads
```

### 5.5 환경 변수 설정

**EC2에서**:

```bash
# .bashrc 파일 편집
nano ~/.bashrc

# 파일 맨 아래에 추가:
export JWT_SECRET="gjITbPwjXp3ZvUfcGx+sxaeB3Mdxcbfur9ZGZkmwgQs="
export DB_PASSWORD="YOUR_RDS_PASSWORD"

# 저장: Ctrl+O, Enter, Ctrl+X

# 환경 변수 적용
source ~/.bashrc

# 확인
echo $JWT_SECRET
echo $DB_PASSWORD
```

### 5.6 애플리케이션 실행 테스트

**EC2에서**:

```bash
cd ~/perfacto

# 애플리케이션 실행
java -jar -Dspring.profiles.active=prod app.jar

# Spring Boot 로그 확인
# "Started ScrdApplication in X.XXX seconds" 메시지 확인

# 브라우저에서 테스트 (새 터미널에서):
curl http://13.125.XXX.XXX:8080/perfacto/every/categories

# JSON 응답 확인되면 성공!

# 애플리케이션 중지: Ctrl+C
```

### 5.7 Systemd 서비스 등록

**EC2에서**:

```bash
# 서비스 파일 생성
sudo nano /etc/systemd/system/perfacto.service
```

다음 내용 입력:

```ini
[Unit]
Description=Perfacto Spring Boot Application
After=syslog.target network.target

[Service]
User=ubuntu
WorkingDirectory=/home/ubuntu/perfacto
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod -Dspring.config.location=file:/home/ubuntu/perfacto/application-prod.yml /home/ubuntu/perfacto/app.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

Environment="JWT_SECRET=gjITbPwjXp3ZvUfcGx+sxaeB3Mdxcbfur9ZGZkmwgQs="
Environment="DB_PASSWORD=YOUR_RDS_PASSWORD"

StandardOutput=journal
StandardError=journal
SyslogIdentifier=perfacto

[Install]
WantedBy=multi-user.target
```

**⚠️ DB_PASSWORD를 실제 RDS 비밀번호로 변경!**

저장: `Ctrl+O`, Enter, `Ctrl+X`

```bash
# Systemd 데몬 리로드
sudo systemctl daemon-reload

# 서비스 시작
sudo systemctl start perfacto

# 서비스 상태 확인
sudo systemctl status perfacto
# "active (running)" 확인

# 부팅 시 자동 시작 설정
sudo systemctl enable perfacto

# 로그 확인
sudo journalctl -u perfacto -f
# Ctrl+C로 종료

# 테스트
curl http://localhost:8080/perfacto/every/categories
```

---

## 6. Nginx 및 보안 설정

### 6.1 Nginx 리버스 프록시 설정

**EC2에서**:

```bash
# Nginx 설정 파일 생성
sudo nano /etc/nginx/sites-available/perfacto
```

다음 내용 입력:

```nginx
server {
    listen 80;
    server_name 13.125.XXX.XXX;  # EC2 퍼블릭 IP 또는 도메인

    client_max_body_size 10M;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # Timeout 설정
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # 정적 파일 (업로드된 이미지)
    location /files/ {
        alias /home/ubuntu/perfacto/uploads/;
        expires 30d;
        add_header Cache-Control "public, immutable";
    }
}
```

**⚠️ server_name을 EC2 IP로 변경!**

저장: `Ctrl+O`, Enter, `Ctrl+X`

```bash
# 심볼릭 링크 생성
sudo ln -s /etc/nginx/sites-available/perfacto /etc/nginx/sites-enabled/

# 기본 사이트 비활성화
sudo rm /etc/nginx/sites-enabled/default

# Nginx 설정 테스트
sudo nginx -t
# "syntax is okay", "test is successful" 확인

# Nginx 재시작
sudo systemctl restart nginx

# 상태 확인
sudo systemctl status nginx
```

### 6.2 방화벽 설정 (UFW)

**EC2에서**:

```bash
# UFW 활성화
sudo ufw allow 22/tcp comment 'SSH'
sudo ufw allow 80/tcp comment 'HTTP'
sudo ufw allow 443/tcp comment 'HTTPS'

# UFW 상태 확인 (아직 활성화 안 함)
sudo ufw status

# UFW 활성화
sudo ufw enable
# "y" 입력

# 확인
sudo ufw status verbose
```

### 6.3 EC2 보안 그룹 업데이트

**AWS 콘솔에서**:

1. EC2 → 인스턴스 → perfacto-server 선택
2. "보안" 탭 → 보안 그룹 클릭
3. "인바운드 규칙" → "인바운드 규칙 편집"
4. **8080 포트 규칙 삭제** (Nginx를 통해서만 접근)
5. "규칙 저장"

### 6.4 RDS 보안 그룹 업데이트

**AWS 콘솔에서**:

1. RDS → perfacto-db 선택
2. "연결 & 보안" → VPC 보안 그룹 클릭
3. "인바운드 규칙" → "인바운드 규칙 편집"
4. 기존 "내 IP" 규칙 외에 추가:
   ```
   유형: MySQL/Aurora
   소스: perfacto-server-sg (EC2 보안 그룹)
   설명: From EC2 instance
   ```
5. "규칙 저장"

---

## 7. 테스트 및 검증

### 7.1 API 테스트

**로컬에서**:

```bash
# 카테고리 조회 (인증 불필요)
curl http://13.125.XXX.XXX/perfacto/every/categories

# 예상 응답:
{
  "success": true,
  "data": [
    {
      "id": 1,
      "code": "restaurant",
      "name": "음식점",
      ...
    }
  ]
}
```

### 7.2 소셜 로그인 테스트

**브라우저에서**:

```
# 카카오 로그인 URL (실제로는 카카오 앱에서 처리됨)
http://13.125.XXX.XXX/perfacto/auth/kakao-login?code=AUTHORIZATION_CODE
```

### 7.3 데이터베이스 테이블 확인

**로컬 또는 EC2에서**:

```bash
mysql -h perfacto-db.xxxxxxxxx.ap-northeast-2.rds.amazonaws.com -u admin -p

USE perfacto;

SHOW TABLES;
# categories, places, users, likes 등 테이블 확인

SELECT * FROM categories;
# 4개 카테고리 확인

EXIT;
```

### 7.4 로그 확인

**EC2에서**:

```bash
# 서비스 로그
sudo journalctl -u perfacto -n 100

# 애플리케이션 로그
tail -f /var/log/perfacto/application.log

# Nginx 액세스 로그
sudo tail -f /var/log/nginx/access.log

# Nginx 에러 로그
sudo tail -f /var/log/nginx/error.log
```

---

## 8. 다음 단계

### 8.1 Flutter 앱 연동

`lib/services/api_service.dart`:
```dart
class ApiConfig {
  static const String baseUrl = 'http://13.125.XXX.XXX';
  // 또는 도메인 연결 후
  // static const String baseUrl = 'https://api.perfacto.com';
}
```

### 8.2 도메인 및 HTTPS 설정 (선택사항)

1. 도메인 구매
2. Route 53 설정
3. Let's Encrypt SSL 인증서 발급
4. Nginx HTTPS 설정

### 8.3 모니터링 설정

1. CloudWatch 메트릭 확인
2. 알람 설정
3. 로그 수집 자동화

---

## 📊 Free Tier 사용량 체크

### AWS 콘솔에서 확인

1. **결제 대시보드**
   - 우측 상단 계정명 → "결제 및 비용 관리"
   - "Free Tier" 메뉴
   - 현재 사용량 확인

2. **주요 확인 항목**
   ```
   EC2 t2.micro: 750시간/월 (31일 = 744시간)
   RDS db.t3.micro: 750시간/월
   스토리지: 30GB (EC2 + RDS)
   데이터 전송: 15GB 아웃바운드/월
   ```

3. **비용 알림 설정 확인**
   - "예산" 메뉴
   - 알림 이메일 수신 확인

---

## 🔧 문제 해결

### RDS 연결 실패
```bash
# 보안 그룹 확인
# 내 IP가 허용되었는지 확인
# EC2 보안 그룹이 허용되었는지 확인
```

### EC2 SSH 연결 실패
```bash
# 키 권한 확인
chmod 400 ~/.ssh/perfacto-key.pem

# 보안 그룹 SSH 규칙 확인
# 22 포트가 내 IP에 열려있는지 확인
```

### 애플리케이션 실행 오류
```bash
# 로그 확인
sudo journalctl -u perfacto -n 100

# 데이터베이스 연결 확인
mysql -h RDS_ENDPOINT -u admin -p

# 포트 확인
sudo netstat -tlnp | grep 8080
```

### Nginx 502 Bad Gateway
```bash
# Spring Boot 서비스 상태 확인
sudo systemctl status perfacto

# 로그 확인
sudo journalctl -u perfacto -f
```

---

## ✅ 완료 체크리스트

- [ ] AWS 계정 생성
- [ ] IAM 사용자 생성
- [ ] 서울 리전 선택
- [ ] RDS MySQL 생성
- [ ] RDS 보안 그룹 설정
- [ ] RDS 연결 테스트
- [ ] EC2 인스턴스 생성
- [ ] 키 페어 다운로드 및 저장
- [ ] EC2 SSH 접속
- [ ] JDK 17 설치
- [ ] Nginx 설치
- [ ] Spring Boot JAR 빌드
- [ ] 프로덕션 설정 파일 생성
- [ ] JAR 파일 EC2 전송
- [ ] Systemd 서비스 등록
- [ ] Nginx 리버스 프록시 설정
- [ ] 방화벽 설정
- [ ] API 테스트 성공
- [ ] 데이터베이스 테이블 확인

---

**축하합니다! AWS 프로덕션 배포 완료!** 🎉
