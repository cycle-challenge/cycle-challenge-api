package com.yeohangttukttak.api.domain.member.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmailVerificationCodeRepository {

    private final StringRedisTemplate template;

    public void save(String email, String code) {
        template.opsForValue()
                .set(email, code, Duration.ofMinutes(3));
    }

    public Optional<String> findByEmail(String email) {
        String code = template.opsForValue()
                .get(email);

        return Optional.ofNullable(code);
    }

}
