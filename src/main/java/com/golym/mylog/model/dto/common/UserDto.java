package com.golym.mylog.model.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;
    private String email;
    private String username;
    private String nickname;
    private String profileImage;
}
