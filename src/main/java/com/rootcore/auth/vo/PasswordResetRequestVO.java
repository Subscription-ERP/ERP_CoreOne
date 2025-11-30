package com.rootcore.auth.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Data
public class PasswordResetRequestVO {

    @NotBlank(message = "아이디는 필수 입력입니다.")
    @Pattern(
            regexp = "^[a-zA-Z0-9_.-]{4,20}$",
            message = "아이디는 4~20자, 영문/숫자/점(.), 하이픈(-), 언더바(_)만 가능합니다."
    )
    private String userId;

    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;
}
