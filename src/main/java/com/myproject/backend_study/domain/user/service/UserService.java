package com.myproject.backend_study.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myproject.backend_study.common.jwt.JwtUtil;
import com.myproject.backend_study.domain.user.dto.request.LoginRequestDto;
import com.myproject.backend_study.domain.user.dto.request.SignupRequestDto;
import com.myproject.backend_study.domain.user.dto.response.LoginResponseDto;
import com.myproject.backend_study.domain.user.dto.response.SignupResponseDto;
import com.myproject.backend_study.domain.user.entity.User;
import com.myproject.backend_study.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Transactional
	public SignupResponseDto signup(SignupRequestDto requestDto) {
		// 1. 이메일 중복 체크
		if (userRepository.existsByEmail(requestDto.getEmail())) {
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다");
		}

		// 2. 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

		// 3. User 앤티티 생성
		User user = User.builder()
			.email(requestDto.getEmail())
			.username(requestDto.getUsername())
			.password(encodedPassword)
			.build();

		// 4. DB 저장
		User savedUser = userRepository.save(user);

		// 5. entity -> DTO
		return SignupResponseDto.from(savedUser);
	}

	@Transactional
	public LoginResponseDto login(LoginRequestDto requestDto) {
		//1.이메일로 회원 찾기
		User user = userRepository.findByEmail(requestDto.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않은 이메일입니다."));

		//2.비밀번호 확인
		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
		}

		// 3.jwt 토큰 생성
		String token = jwtUtil.generateTocken(user.getEmail(), user.getId());

		return LoginResponseDto.of(
			token,
			user.getId(),
			user.getEmail(),
			user.getUsername()
		);
	}
}
