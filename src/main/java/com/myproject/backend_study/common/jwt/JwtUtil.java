package com.myproject.backend_study.common.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	// JWT 서명에 사용할 비밀키입니다
	// application.yml에서 값을 주입받아 사용
	private final SecretKey secretKey;

	// 토큰의 유효시간 (1시간)
	private static final long EXPIRATION_TIME = 1000 * 60 * 60;

	public JwtUtil(@Value("${jwt.secret}") String secret) {
		// 비밀키를 생성 : HMAC SHA 알고리즘을 사용하며, 키는 충분히 길어야 안전
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
	}

	// JWT 토큰을 생성하는 메서드
	public String generateTocken(String email, Long userId) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

		return Jwts.builder()
			.subject(email)  // 토큰의 주체(사용자 이메일)
			.claim("userId", userId)  // 추가 정보(사용자 ID)
			.issuedAt(now)  // 토큰 발급 시간
			.expiration(expiration)  // 토큰 만료 시간
			.signWith(secretKey)  // 서명
			.compact();  // 문자열로 변환
	}

	// JWT 토큰에서 이메일을 추출하는 메서드입니다
	public String getEmailFromToken(String token) {
		Claims claims = parseToken(token);
		return claims.getSubject();
	}

	// JWT 토큰에서 사용자 ID를 추출하는 메서드입니다
	public Long getUserIdFromToken(String token) {
		Claims claims = parseToken(token);
		return claims.get("userId", Long.class);
	}

	// JWT 토큰을 파싱하고 검증하는 메서드입니다
	private Claims parseToken(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)  // 서명 검증
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	// JWT 토큰이 유효한지 검증하는 메서드입니다
	public boolean validateToken(String token) {
		try {
			parseToken(token);
			return true;
		} catch (Exception e) {
			// 토큰이 만료되었거나, 서명이 잘못되었거나, 형식이 잘못된 경우
			return false;
		}
	}
}
