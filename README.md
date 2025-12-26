# 🗺️ Perfacto Server - 포항 지역 장소 추천 플랫폼 백엔드

> **지도 기반 포항 지역 맛집/카페/관광지 추천 플랫폼 - 백엔드 레포지토리**
>
> 프로젝트명: **Perfacto Server**

[![Perfacto 앱 데모 영상](https://img.youtube.com/vi/ahx63G0vqEk/0.jpg)](https://youtube.com/shorts/ahx63G0vqEk?feature=share)

- **기간**: 2024.10.23 ~ 2025.05.20 (총 7개월)
- [📱 Perfacto 앱 데모 영상](https://youtube.com/shorts/ahx63G0vqEk?feature=share)

---

## 📌 프로젝트 개요

포항 지역 주민과 방문객을 위한 맛집, 카페, 관광지, 숙박시설 등을 통합 관리하는 지도 기반 장소 추천 플랫폼입니다. 사용자들은 1km 반경 내 장소를 검색하고, 3단계 리뷰 시스템을 통해 상세한 평가를 남기며, ELO 랭킹 시스템으로 신뢰도 높은 장소 순위를 확인할 수 있습니다.

- **위치 기반 1km 필터링**: 현재 위치 기반 주변 장소 실시간 추천
- **3단계 리뷰 시스템**: 다양성(Variety), 분위기(Atmosphere), 추천도(Recommend) 평가
- **ELO 랭킹 시스템**: 장소 간 비교를 통한 신뢰도 높은 순위 산정
- **소셜 로그인**: 카카오, 구글, 애플 로그인 지원
- **실시간 알림**: 리뷰, 댓글 등 활동 알림 제공

---

## 🧩 주요 기능

### ✅ 인증/회원 시스템
- **Spring Security + JWT 기반 인증 시스템 구축**
- 소셜 로그인(OAuth2.0) 연동 (카카오, 구글, 애플)
- **RTR(Refresh Token Rotation)** 구조로 UX 개선
- Redis 기반 토큰 관리 및 자동 갱신

### ✅ 장소 API
- **1km 반경 위치 기반 필터링** (Haversine 공식 활용)
- QueryDSL 기반 다중 조건 검색 (카테고리, 평점, 거리)
- ELO 랭킹 기반 장소 순위 제공
- 장소 상세 정보 및 리뷰 통계 조회

### ✅ 리뷰 API
- **3단계 평가 시스템**: Variety, Atmosphere, Recommend
- 이미지 업로드 지원 (최대 5장)
- 리뷰 등록 시 자동 포인트 지급
- ELO 랭킹 시스템과 연동된 장소 평가

### ✅ ELO 랭킹 시스템
- 장소 간 1:1 비교를 통한 상대적 순위 산정
- K-factor 기반 동적 점수 조정
- 카테고리별 랭킹 관리
- 실시간 랭킹 업데이트

### ✅ 배포 / 인프라 운영
- **Nginx + HTTPS 서버 구성**
- **Docker + Docker Compose** 기반 컨테이너화
- AWS EC2 + RDS 인프라 관리
- GitHub Actions 기반 **CI/CD 자동 배포 파이프라인 구축**

---

## 💡 사용 기술 스택

### 🧱 Backend
- **Spring Boot 3.3.5**, Spring Security, JWT, OAuth2.0
- **Redis** (토큰 관리, 캐싱)
- **MySQL** (메인 DB), **MongoDB** (로그 저장)
- Spring Data JPA, QueryDSL
- Haversine Formula (위치 기반 필터링)

### 🛠 Infra / DevOps
- **Docker**, **Docker Compose**, **Nginx**
- AWS EC2, RDS, S3, Route 53
- GitHub Actions (CI/CD)
- Let's Encrypt (HTTPS)

### 📱 Mobile
- **Flutter** (크로스 플랫폼)
- Provider (상태 관리)
- Google Maps API (지도 기능)
- Firebase (푸시 알림)

### 📦 기타
- ELO Rating Algorithm (장소 랭킹)
- 위치 기반 필터링 (1km 반경)
- 이미지 업로드 및 최적화

---

## 🏗️ 시스템 아키텍처

```
[Flutter App] <--> [Nginx] <--> [Spring Boot Server]
                                      |
                    +----------------+----------------+
                    |                |                |
                 [MySQL]          [Redis]        [MongoDB]
                (메인 DB)      (토큰/캐시)       (로그)
```

---

## 📞 문의
> 개발자 이민규 · E-mail: keilvher@gmail.com

---

본 프로젝트는 포항 지역 특화 서비스로, 실제 사용자의 위치 기반 맞춤형 장소 추천을 목표로 개발되었습니다. ELO 랭킹 시스템을 통해 신뢰도 높은 장소 평가를 제공하며, 지속적인 사용자 피드백을 반영하여 개선하고 있습니다.
