package com.myproject.backend_study.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponseDto {

	private String token;  // JWT 토큰
	private Long userId;
	private String email;
	private String username;

	public static LoginResponseDto of(String token, Long userId, String email, String username) {
		return LoginResponseDto.builder()
			.token(token)
			.userId(userId)
			.email(email)
			.username(username)
			.build();
	}
}
