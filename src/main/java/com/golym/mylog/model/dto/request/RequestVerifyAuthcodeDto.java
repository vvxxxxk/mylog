package com.golym.mylog.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestVerifyAuthcodeDto {

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @Pattern(regexp = "^\\d{6}$", message = "인증번호는 숫자 6자리 형식입니다.")
    @NotBlank(message = "인증번호를 입력해주세요.")
    private String authcode;
}
