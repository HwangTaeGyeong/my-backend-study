package com.myproject.backend_study.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor	// JSON 역직렬화를 위해 기본 생성자가 필요합니다
@AllArgsConstructor
public class SignupRequestDto {

	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "올바른 이메일 형식이 아닙니다")
	@Size(max = 100, message = "이메일은 100자를 초과할 수 없습니다")
	private String email;

	@NotBlank(message = "사용자명은 필수입니다")
	@Size(min = 2, max = 20, message = "사용자명은 2자 이상 20자 이하여야 합니다")
	private String username;

	@NotBlank(message = "비밀번호는 필수입니다")
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
		message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다"
	)
	private String password;

}
