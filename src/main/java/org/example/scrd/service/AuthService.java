package org.example.scrd.service;


import lombok.RequiredArgsConstructor;
import org.example.scrd.domain.User;
import org.example.scrd.dto.AppleDto;
import org.example.scrd.dto.Tier;
import org.example.scrd.dto.UserDto;
import org.example.scrd.dto.SignUpRequest;
import org.example.scrd.dto.LoginRequest;
import org.example.scrd.domain.Role;
import org.example.scrd.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RandomNicknameService randomNicknameService;
    private final PasswordEncoder passwordEncoder;

    // 카카오 로그인 로직
    public UserDto kakaoLogin(UserDto dto) {
        User user = userRepository
                .findByKakaoId(dto.getKakaoId())
                .orElseGet(() -> {
                    User newUser = User.from(dto);
                    newUser.setNickName(randomNicknameService.generateUniqueNickname());
                    newUser.setTier(Tier.ONE);
                    return userRepository.save(newUser);
                });

        user.setEmail(dto.getEmail());
        user.setProfileImageUrl(dto.getProfileImageUrl());
        user.setName(dto.getName());

        // 👇 기존 유저인데 닉네임이 없는 경우
        if (user.getNickName() == null || user.getNickName().isBlank()) {
            user.setNickName(randomNicknameService.generateUniqueNickname());
            userRepository.save(user); // 👈 변경 즉시 DB에 반영
        }


        return UserDto.from(user);
    }

    // Apple 로그인 로직
    public UserDto appleLogin(UserDto dto) {
        User user = userRepository
                .findByAppleId(dto.getAppleId())  // AppleDto의 getId() 사용
                .orElseGet(() -> {
                    User newUser = User.from(dto);
                    newUser.setNickName(randomNicknameService.generateUniqueNickname());
                    newUser.setTier(Tier.ONE);
                    return userRepository.save(newUser);
                });

        // Apple에서 받은 정보로 업데이트
        user.setEmail(dto.getEmail());
        user.setAppleId(dto.getAppleId()); // Apple ID 설정

        // 기존 유저인데 닉네임이 없는 경우
        if (user.getNickName() == null || user.getNickName().isBlank()) {
            user.setNickName(randomNicknameService.generateUniqueNickname());
            userRepository.save(user);
        }

        return UserDto.from(user);
    }

    // Naver 로그인 로직
    public UserDto naverLogin(UserDto dto) {
        User user = userRepository
                .findByNaverId(dto.getNaverId())
                .orElseGet(() -> {
                    User newUser = User.from(dto);
                    newUser.setNickName(randomNicknameService.generateUniqueNickname());
                    newUser.setTier(Tier.ONE);
                    return userRepository.save(newUser);
                });

        // 네이버에서 받은 정보로 업데이트
        user.setEmail(dto.getEmail());
        user.setProfileImageUrl(dto.getProfileImageUrl());
        user.setName(dto.getName());
        user.setNaverId(dto.getNaverId());

        // 기존 유저인데 닉네임이 없는 경우
        if (user.getNickName() == null || user.getNickName().isBlank()) {
            user.setNickName(randomNicknameService.generateUniqueNickname());
            userRepository.save(user);
        }

        return UserDto.from(user);
    }

    // 사용자 ID로 로그인한 사용자 정보 조회
    public User getLoginUser(Long userId) {
        // 사용자 ID로 사용자를 조회, 없으면 예외 발생
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
    }

    // 이메일 회원가입
    public User signUp(SignUpRequest request) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 새 사용자 생성
        User newUser = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .nickName(request.getNickname())
                .name(request.getNickname())
                .role(Role.ROLE_USER)
                .tier(Tier.ONE)
                .build();

        return userRepository.save(newUser);
    }

    // 이메일 로그인
    public User emailLogin(LoginRequest request) {
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return user;
    }
}

