package com.myproject.backend_study.domain.user.dto.response;

import java.time.LocalDateTime;

import com.myproject.backend_study.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignupResponseDto {

	private Long userId;
	private String email;
	private String username;
	private LocalDateTime createdAt;

	// Entity를 DTO로 변환하는 정적 팩토리 메서드입니다
	// 이렇게 하면 Entity와 DTO 사이의 변환 로직을 한 곳에서 관리할 수 있습니다
	public static SignupResponseDto from(User user) {
		return SignupResponseDto.builder()
			.userId(user.getId())
			.email(user.getEmail())
			.username(user.getUsername())
			.createdAt(user.getCreatedAt())
			.build();
	}
}
