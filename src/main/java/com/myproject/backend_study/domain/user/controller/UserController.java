package com.myproject.backend_study.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	// 현재 로그인한 사용자 정보를 반환하는 API입니다
	@GetMapping("/me")
	public ResponseEntity<CurrentUser> getCurrentUser(CurrentUser currentUser) {
		// CurrentUser 객체가 자동으로 주입됩니다
		return ResponseEntity.ok(currentUser);
	}
}
