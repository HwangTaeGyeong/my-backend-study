package com.myproject.backend_study.common.security;

import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.myproject.backend_study.domain.user.entity.User;
import com.myproject.backend_study.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

	private final UserRepository userRepository;

	// 이 리졸버가 어떤 파라미터를 처리할지 결정합니다
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// CurrentUser 타입의 파라미터만 처리
		return parameter.getParameterType().equals(CurrentUser.class);
	}

	// 실제로 파라미터 값을 생성하는 메서드입니다
	@Override
	public Object resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory) throws Exception {

		// SecurityContext에서 인증 정보를 가져옵니다
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		// UserDetails에서 이메일을 추출합니다
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String email = userDetails.getUsername();

		// 이메일로 User 엔티티를 조회합니다
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다"));

		// CurrentUser 객체를 만들어서 반환합니다
		return new CurrentUser(user.getId(), user.getEmail(), user.getUsername());
	}
}
