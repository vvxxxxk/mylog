package com.golym.mylog.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestSignupDto {
    private String email;
    private String password;
    private String username;
    private String nickname;
}
