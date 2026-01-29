package com.myproject.backend_study.common.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		// 1단계: Authorization 헤더에서 토큰을 추출합니다
		String authorizationHeader = request.getHeader("Authorization");

		String token = null;
		String email = null;

		// Bearer 토큰 형식인지 확인하고 토큰을 추출
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7); // "Bearer " 이후의 문자열
			try {
				email = jwtUtil.getEmailFromToken(token);
			} catch (Exception e) {
				log.error("토큰에서 이메일 추출 실패: {}", e.getMessage());
			}
		}

		// 2단계: 토큰이 유효하고, 아직 인증되지 않은 경우 인증 처리
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			// 3단계: DB에서 사용자 정보를 조회합니다
			UserDetails userDetails = userDetailsService.loadUserByUsername(email);

			// 4단계: 토큰이 유효한지 최종 확인합니다
			if (jwtUtil.validateToken(token)) {

				// 5단계: 인증 객체를 생성합니다
				UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(
						userDetails,
						null,    // credentials는 null로 설정 (비밀번호 정보는 필요없음)
						userDetails.getAuthorities()    // 권한 정보
					);

				// 요청 정보를 추가합니다 (IP 주소 등)
				authenticationToken.setDetails(
					new WebAuthenticationDetailsSource().buildDetails(request));

				// 6단계: SecurityContext에 인증 정보를 저장합니다
				// 이렇게 하면 이후 모든 과정에서 "인증된 사용자"로 인식됩니다
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);

				log.info("사용자 인증 완료: {}", email);
			}
		}

		// 7단계: 다음 필터로 요청을 전달합니다
		filterChain.doFilter(request, response);
	}
}
