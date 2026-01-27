package com.myproject.backend_study.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myproject.backend_study.domain.user.dto.request.SignupRequestDto;
import com.myproject.backend_study.domain.user.dto.response.SignupResponseDto;
import com.myproject.backend_study.domain.user.entity.User;
import com.myproject.backend_study.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

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
}
