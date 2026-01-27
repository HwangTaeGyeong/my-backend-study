package com.myproject.backend_study.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.backend_study.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// 이메일로 회원을 찾는 메서드
	Optional<User> findByEmail(String email);

	// 이메일이 존재하는지 확인하는 메서드
	boolean existsByEmail(String email);

}
