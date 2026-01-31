package com.myproject.backend_study.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.backend_study.common.security.CurrentUser;
import com.myproject.backend_study.domain.user.dto.request.LoginRequestDto;
import com.myproject.backend_study.domain.user.dto.request.SignupRequestDto;
import com.myproject.backend_study.domain.user.dto.response.LoginResponseDto;
import com.myproject.backend_study.domain.user.dto.response.SignupResponseDto;
import com.myproject.backend_study.domain.user.dto.response.UserInfoDto;
import com.myproject.backend_study.domain.user.service.UserDetailsImpl;
import com.myproject.backend_study.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<SignupResponseDto> signup(
		@Valid @RequestBody SignupRequestDto requestDto
	) {
		SignupResponseDto responseDto = userService.signup(requestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(
		@Valid @RequestBody LoginRequestDto requestDto
	) {
		LoginResponseDto responseDto = userService.login(requestDto);

		return ResponseEntity.ok(responseDto);
	}

	// 현재 로그인한 사용자 정보를 반환하는 API
	// @AuthenticationPrincipal을 사용하면 SecurityContext에서
	// 인증 정보를 자동으로 꺼내서 주입해줍니다
	@GetMapping("/me")
	public ResponseEntity<UserInfoDto> getCurrentUser(
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		// UserDetailsImpl에서 필요한 정보를 꺼내서 DTO로 만들어 반환합니다
		UserInfoDto userInfo = new UserInfoDto(
			userDetails.getUserId(),
			userDetails.getEmail(),
			userDetails.getUsernameField()
		);

		return ResponseEntity.ok(userInfo);
	}
}
