package com.golym.mylog.model.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "authEmail", timeToLive = 60*5*12)
public class AuthEmail {

    @Id
    private String email;
    private String authcode;

    public AuthEmail(String email, String authcode) {
        this.email = email;
        this.authcode = authcode;
    }
}
