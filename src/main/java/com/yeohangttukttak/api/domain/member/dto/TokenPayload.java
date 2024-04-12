package com.yeohangttukttak.api.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public  class TokenPayload {

    private String email;
    private Long iat;
    private Long exp;

    public TokenPayload(String email, Instant now, Long expSeconds) {
        this.email = email;
        Instant expiration = now.plusSeconds(expSeconds);

        // Convert from milliseconds to seconds for JWT standard compliance
        this.iat = now.getEpochSecond();
        this.exp = expiration.getEpochSecond();
    }
}