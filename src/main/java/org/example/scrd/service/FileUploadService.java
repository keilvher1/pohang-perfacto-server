package org.example.scrd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scrd.domain.Place;
import org.example.scrd.domain.PlaceImage;
import org.example.scrd.domain.User;
import org.example.scrd.exception.NotFoundException;
import org.example.scrd.repo.PlaceImageRepository;
import org.example.scrd.repo.PlaceRepository;
import org.example.scrd.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 파일 업로드 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileUploadService {

    private final PlaceImageRepository placeImageRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    @Value("${file.upload.path:/uploads}")
    private String uploadPath;

    @Value("${file.upload.url-prefix:/files}")
    private String urlPrefix;

    /**
     * 장소 이미지 업로드
     */
    @Transactional
    public String uploadPlaceImage(Long placeId, MultipartFile file, Long userId, Integer displayOrder) {
        log.info("Uploading image for place {}", placeId);

        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> new NotFoundException("장소를 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        // 파일 저장
        String storedFileName = saveFile(file);
        String imageUrl = urlPrefix + "/" + storedFileName;

        // PlaceImage 엔티티 생성
        PlaceImage placeImage = PlaceImage.builder()
            .place(place)
            .imageUrl(imageUrl)
            .originalFileName(file.getOriginalFilename())
            .storedFileName(storedFileName)
            .fileSize(file.getSize())
            .displayOrder(displayOrder != null ? displayOrder : 999)
            .uploadedBy(user)
            .build();

        placeImageRepository.save(placeImage);

        log.info("Image uploaded successfully: {}", imageUrl);
        return imageUrl;
    }

    /**
     * 프로필 이미지 업로드
     */
    @Transactional
    public String uploadProfileImage(MultipartFile file, Long userId) {
        log.info("Uploading profile image for user {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        // 파일 저장
        String storedFileName = saveFile(file);
        String imageUrl = urlPrefix + "/" + storedFileName;

        // 사용자 프로필 이미지 URL 업데이트
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);

        log.info("Profile image uploaded successfully: {}", imageUrl);
        return imageUrl;
    }

    /**
     * 파일 저장 (로컬 스토리지)
     * TODO: 추후 S3로 변경 가능
     */
    private String saveFile(MultipartFile file) {
        try {
            // 업로드 디렉토리 생성
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 파일명 생성 (UUID + 원본 확장자)
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
            String storedFileName = UUID.randomUUID().toString() + extension;

            // 파일 저장
            Path filePath = Paths.get(uploadPath, storedFileName);
            Files.write(filePath, file.getBytes());

            return storedFileName;

        } catch (IOException e) {
            log.error("File upload failed", e);
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }

    /**
     * 이미지 삭제
     */
    @Transactional
    public void deletePlaceImage(Long imageId, Long userId) {
        log.info("Deleting image {}", imageId);

        PlaceImage placeImage = placeImageRepository.findById(imageId)
            .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다."));

        // 작성자만 삭제 가능
        if (!placeImage.getPlace().getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("이미지를 삭제할 권한이 없습니다.");
        }

        // 파일 시스템에서 파일 삭제
        try {
            Path filePath = Paths.get(uploadPath, placeImage.getStoredFileName());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("Failed to delete file from filesystem", e);
        }

        // DB에서 삭제
        placeImageRepository.delete(placeImage);

        log.info("Image deleted successfully");
    }
}
