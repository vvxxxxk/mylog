package com.golym.mylog.model.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "viewCountLog", timeToLive = 60*60*24)
public class ViewCountLogEntity {

    @Id
    private String id;

    private String postId;
    private String userIp;

    public ViewCountLogEntity(String postId, String userIp) {
        this.id = postId + ":" + userIp;
        this.postId = postId;
        this.userIp = userIp;
    }
}
