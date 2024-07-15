package com.golym.mylog.model.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "authEmail", timeToLive = 60*5*12)
public class AuthEmailEntity {

    @Id
    private String email;
    private String authcode;

    public AuthEmailEntity(String email, String authcode) {
        this.email = email;
        this.authcode = authcode;
    }
}
