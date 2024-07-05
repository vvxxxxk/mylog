package com.golym.mylog.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseReissueTokenDto {

    private String userId;
    private String accessToken;
    private String refreshToken;
}
