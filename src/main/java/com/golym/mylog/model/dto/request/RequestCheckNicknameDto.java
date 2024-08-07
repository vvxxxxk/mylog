package com.golym.mylog.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestCheckNicknameDto {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
}