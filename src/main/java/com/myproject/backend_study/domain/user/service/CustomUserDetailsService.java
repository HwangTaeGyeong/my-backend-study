package com.myproject.backend_study.domain.user.service;


import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.myproject.backend_study.domain.user.entity.User;
import com.myproject.backend_study.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		// 1. 이메일로 사용자 조회
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다." + email));

		// 2. User 엔티티를 Spring Security의 UserDetails로 변환
		// UserDetails는 Spring Security가 사용자 정보를 다루는 표준 인터페이스
		return org.springframework.security.core.userdetails.User.builder()
			.username(user.getEmail())
			.password(user.getPassword())
			.authorities(new ArrayList<>())  // 일단 권한은 비워둡니다
			.build();
	}
}
