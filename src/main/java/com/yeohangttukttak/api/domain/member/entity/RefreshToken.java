package com.yeohangttukttak.api.domain.member.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "RT")
public class RefreshToken {

    @Id
    private String userID;

    @Indexed
    private String token;

    @TimeToLive
    private Long TTL;

}
