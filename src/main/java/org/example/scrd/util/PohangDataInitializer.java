package org.example.scrd.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.domain.Category;
import org.example.scrd.domain.Place;
import org.example.scrd.repo.CategoryRepository;
import org.example.scrd.repo.PlaceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 포항 지역 장소 데이터 초기화
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PohangDataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final PlaceRepository placeRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 이미 데이터가 있으면 스킵
        if (placeRepository.count() > 0) {
            log.info("⏭️  포항 장소 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
            return;
        }

        log.info("🚀 포항 지역 장소 데이터 초기화를 시작합니다...");

        // 1. 카테고리 생성
        initializeCategories();

        // 2. 장소 데이터 생성
        initializePlaces();

        log.info("✅ 포항 지역 장소 데이터 초기화 완료!");
    }

    private void initializeCategories() {
        List<Category> categories = new ArrayList<>();

        // 카테고리가 이미 존재하는지 확인하고 없을 때만 추가
        if (!categoryRepository.existsByCode("restaurant")) {
            categories.add(Category.builder()
                    .code("restaurant")
                    .name("음식점")
                    .nameEn("Restaurant")
                    .description("포항의 맛집과 음식점")
                    .displayOrder(1)
                    .isActive(true)
                    .build());
        }

        if (!categoryRepository.existsByCode("cafe")) {
            categories.add(Category.builder()
                    .code("cafe")
                    .name("카페")
                    .nameEn("Cafe")
                    .description("포항의 카페와 디저트")
                    .displayOrder(2)
                    .isActive(true)
                    .build());
        }

        if (!categoryRepository.existsByCode("attraction")) {
            categories.add(Category.builder()
                    .code("attraction")
                    .name("관광지")
                    .nameEn("Attraction")
                    .description("포항의 관광명소")
                    .displayOrder(3)
                    .isActive(true)
                    .build());
        }

        if (!categoryRepository.existsByCode("accommodation")) {
            categories.add(Category.builder()
                    .code("accommodation")
                    .name("숙박")
                    .nameEn("Accommodation")
                    .description("포항의 숙박시설")
                    .displayOrder(4)
                    .isActive(true)
                    .build());
        }

        if (!categories.isEmpty()) {
            categoryRepository.saveAll(categories);
            log.info("📁 카테고리 {} 개 생성 완료", categories.size());
        } else {
            log.info("📁 카테고리가 이미 존재합니다.");
        }
    }

    private void initializePlaces() {
        Category restaurant = categoryRepository.findByCode("restaurant").orElseThrow();
        Category cafe = categoryRepository.findByCode("cafe").orElseThrow();
        Category attraction = categoryRepository.findByCode("attraction").orElseThrow();
        Category accommodation = categoryRepository.findByCode("accommodation").orElseThrow();

        List<Place> places = new ArrayList<>();

        // ========== 관광지 ==========
        places.add(createPlace(
                "호미곶 해맞이광장",
                "한반도에서 가장 먼저 해가 뜨는 곳으로 유명한 포항의 대표 관광지. 상생의 손 조형물이 상징적입니다.",
                "남구", "경상북도 포항시 남구 호미곶면 해맞이로 150",
                36.0764, 129.5651, attraction,
                "054-270-5855", "http://www.homigot.or.kr", "연중무휴 24시간"
        ));

        places.add(createPlace(
                "영일대해수욕장",
                "깨끗한 백사장과 푸른 바다가 어우러진 포항의 대표 해수욕장. 영일대 전망대와 스페이스워크가 있습니다.",
                "북구", "경상북도 포항시 북구 두호동 685-1",
                36.0691, 129.3822, attraction,
                "054-245-6801", null, "연중무휴 24시간"
        ));

        places.add(createPlace(
                "죽도시장",
                "60년 전통의 포항 최대 전통시장. 신선한 해산물과 포항 과메기가 유명합니다.",
                "북구", "경상북도 포항시 북구 죽도시장 15길 19",
                36.0328, 129.3655, attraction,
                "054-242-0911", "http://www.jukdomarket.com", "매일 05:00-20:00"
        ));

        places.add(createPlace(
                "포스코 역사관",
                "포스코의 역사와 제철 과정을 체험할 수 있는 기업 박물관",
                "남구", "경상북도 포항시 남구 동해안로 6261",
                36.0089, 129.3422, attraction,
                "054-220-7701", "http://www.poscomuseum.com", "화-일 10:00-17:00 (월요일 휴관)"
        ));

        places.add(createPlace(
                "내연산 보경사",
                "신라시대 창건된 고찰로 12폭포가 유명한 명승지",
                "북구", "경상북도 포항시 북구 송라면 중산리 216",
                36.2089, 129.2567, attraction,
                "054-262-1117", "http://www.bogyeongsa.or.kr", "매일 06:00-18:00"
        ));

        // ========== 음식점 ==========
        places.add(createPlace(
                "구룡포 과메기 거리",
                "포항의 명물 과메기를 맛볼 수 있는 음식점 거리. 신선한 과메기와 해산물 요리가 가득합니다.",
                "남구", "경상북도 포항시 남구 구룡포읍 구룡포길 일대",
                35.9908, 129.5656, restaurant,
                "054-276-8800", null, "가게별 상이"
        ));

        places.add(createPlace(
                "죽도시장 물회거리",
                "싱싱한 회와 물회를 저렴한 가격에 즐길 수 있는 죽도시장 내 먹자골목",
                "북구", "경상북도 포항시 북구 죽도시장길 일대",
                36.0331, 129.3658, restaurant,
                "054-242-0911", null, "매일 09:00-21:00"
        ));

        places.add(createPlace(
                "모포갈비 본점",
                "포항에서 40년 전통의 소갈비 맛집. 숯불에 구운 갈비가 일품입니다.",
                "북구", "경상북도 포항시 북구 학산동 439-7",
                36.0456, 129.3589, restaurant,
                "054-241-8292", null, "매일 11:00-22:00"
        ));

        places.add(createPlace(
                "포항물회",
                "신선한 회와 시원한 육수가 일품인 물회 전문점",
                "남구", "경상북도 포항시 남구 상도동 298-1",
                36.0189, 129.3511, restaurant,
                "054-284-3456", null, "매일 10:00-21:00"
        ));

        places.add(createPlace(
                "영일대 횟집거리",
                "영일대 해변을 바라보며 신선한 회를 즐길 수 있는 횟집 거리",
                "북구", "경상북도 포항시 북구 두호동 일대",
                36.0695, 129.3825, restaurant,
                null, null, "가게별 상이"
        ));

        // ========== 카페 ==========
        places.add(createPlace(
                "카페 칠포리",
                "영일대 해변이 보이는 오션뷰 카페. 루프탑에서 바다를 감상할 수 있습니다.",
                "북구", "경상북도 포항시 북구 두호동 685-5",
                36.0688, 129.3819, cafe,
                "054-247-2233", null, "매일 09:00-22:00"
        ));

        places.add(createPlace(
                "스타벅스 포항영일대점",
                "영일대 해변 바로 앞에 위치한 스타벅스. 오션뷰를 즐기며 커피를 마실 수 있습니다.",
                "북구", "경상북도 포항시 북구 해도동 1095",
                36.0712, 129.3834, cafe,
                "1522-3232", null, "매일 07:00-22:00"
        ));

        places.add(createPlace(
                "카페 더포트",
                "구룡포항 앞 바다가 보이는 카페. 아메리카노와 수제 디저트가 맛있습니다.",
                "남구", "경상북도 포항시 남구 구룡포읍 구룡포리 165-3",
                35.9915, 129.5678, cafe,
                "054-276-7788", null, "매일 10:00-21:00"
        ));

        places.add(createPlace(
                "커피명가 포항본점",
                "포항 로컬 카페 브랜드. 원두 볶는 향기와 고소한 커피가 특징입니다.",
                "북구", "경상북도 포항시 북구 장성동 1446-9",
                36.0378, 129.3601, cafe,
                "054-252-5525", null, "매일 08:00-22:00"
        ));

        places.add(createPlace(
                "호미곶 카페거리",
                "호미곶 해맞이광장 주변의 카페들. 해돋이를 보며 커피를 즐길 수 있습니다.",
                "남구", "경상북도 포항시 남구 호미곶면 대보리 일대",
                36.0768, 129.5655, cafe,
                null, null, "가게별 상이"
        ));

        // ========== 숙박 ==========
        places.add(createPlace(
                "포항 파라다이스 호텔",
                "영일대 해수욕장이 보이는 특급 호텔. 편안한 객실과 우수한 서비스를 제공합니다.",
                "북구", "경상북도 포항시 북구 두호동 693",
                36.0698, 129.3828, accommodation,
                "054-245-7000", "http://www.paradisehotel.co.kr", "체크인 15:00 / 체크아웃 11:00"
        ));

        places.add(createPlace(
                "라한호텔 포항",
                "포항 시내 중심부에 위치한 비즈니스 호텔. 깔끔한 시설과 편리한 위치가 장점입니다.",
                "북구", "경상북도 포항시 북구 우창동 798-3",
                36.0289, 129.3478, accommodation,
                "054-230-0000", "http://www.lahanhotels.com", "체크인 15:00 / 체크아웃 12:00"
        ));

        places.add(createPlace(
                "호미곶 펜션타운",
                "호미곶 근처의 펜션 단지. 바다가 보이는 독채 펜션들이 모여있습니다.",
                "남구", "경상북도 포항시 남구 호미곶면 대보리 일대",
                36.0755, 129.5642, accommodation,
                "054-284-5566", null, "체크인 15:00 / 체크아웃 11:00"
        ));

        places.add(createPlace(
                "포항 게스트하우스 골목",
                "저렴한 가격의 게스트하우스들이 모여있는 골목. 배낭여행객들에게 인기입니다.",
                "북구", "경상북도 포항시 북구 우창동 일대",
                36.0312, 129.3489, accommodation,
                null, null, "업소별 상이"
        ));

        places.add(createPlace(
                "영일대 오션뷰 펜션",
                "영일대 해변이 한눈에 보이는 펜션. 가족 단위 여행객에게 적합합니다.",
                "북구", "경상북도 포항시 북구 두호동 일대",
                36.0702, 129.3831, accommodation,
                "054-247-3344", null, "체크인 15:00 / 체크아웃 11:00"
        ));

        placeRepository.saveAll(places);
        log.info("📍 장소 {} 개 생성 완료", places.size());
        log.info("   - 관광지: 5개");
        log.info("   - 음식점: 5개");
        log.info("   - 카페: 5개");
        log.info("   - 숙박: 5개");
    }

    private Place createPlace(
            String name, String description, String district, String address,
            Double latitude, Double longitude, Category category,
            String phoneNumber, String website, String businessHours) {

        return Place.builder()
                .name(name)
                .description(description)
                .district(district)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .category(category)
                .phoneNumber(phoneNumber)
                .website(website)
                .businessHours(businessHours)
                .likeCount(0)
                .bookmarkCount(0)
                .viewCount(0)
                .isActive(true)
                .build();
    }
}
