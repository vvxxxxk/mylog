package com.golym.mylog.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RequestCheckEmailDto {

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
}
