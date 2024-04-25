package com.yeohangttukttak.api.domain.member.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final StringRedisTemplate template;

    public void save(Long memberId, String token, Long expSeconds) {
        template.opsForValue()
                .set("RT:" + memberId, token, Duration.ofSeconds(expSeconds));
    }

    public Optional<String> findById(Long memberId) {
        String code = template.opsForValue()
                .get("RT:" + memberId);

        return Optional.ofNullable(code);
    }

}
