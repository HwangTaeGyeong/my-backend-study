package com.myproject.backend_study.common.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrentUser {
	private Long userId;
	private String email;
	private String username;
}