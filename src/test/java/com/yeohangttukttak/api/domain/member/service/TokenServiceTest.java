package com.yeohangttukttak.api.domain.member.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeohangttukttak.api.domain.member.dto.TokenPayload;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Base64;

class TokenServiceTest {

    private final TokenService tokenService =
            new TokenService("test", new ObjectMapper());

    @Test
    public void 발급_해독_정상_케이스() throws Exception {
        // given
        String email = "heebeom@example.com";
        Instant now = Instant.now();
        Long expSeconds = 30L;

        // when
        String accessToken = tokenService.issue(email, now, expSeconds);
        TokenPayload payload = tokenService.decode(accessToken);

        // then
        Assertions.assertThat(payload.getEmail())
                .as("해독한 AccessToken의 내용이 달라지지 않아야 한다")
                .isEqualTo(email);

    }

    @Test
    public void 토큰_유효기간_만료_케이스() {
        // given
        String email = "heebeom@example.com";
        Long expSeconds = 30L;

        // 발급 시간을 40초 전으로 설정해 바로 만료되도록 설정한다.
        Instant now = Instant.now().minusSeconds(40L);

        // when, then
        String accessToken = tokenService.issue(email, now, expSeconds);

        Assertions.assertThatThrownBy(() ->
                tokenService.decode(accessToken))
                .as("토큰의 만료 시간이 지난 경우 예외를 발생 한다.")
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ApiErrorCode.AUTHORIZATION_EXPIRED.name());
    }

    @Test
    public void 토큰_서명_위조_케이스() throws Exception {
        // given
        String email = "heebeom@example.com";
        Instant now = Instant.now();
        Long expSeconds = 30L;

        String token = tokenService.issue(email, now, expSeconds);

        // 토큰의 내용을 위조 한다. (Email을 변경)
        String[] parts = token.split("\\.");
        String fakePayload = Base64.getEncoder().encodeToString("{\"email\":\"heejin@exmple.com\"}".getBytes());
        String fakeToken = parts[0] + "." + fakePayload + "." + parts[2];

        // when, then
        Assertions.assertThatThrownBy(() ->
                tokenService.decode(fakeToken))
                .as("임의로 토큰 내용을 조작한 경우 예외를 발생한다.")
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ApiErrorCode.INVALIDED_AUTHORIZATION.name());
    }

    @Test
    public void 잘못된_토큰_형식_케이스() throws Exception {
        // given
        String weirdToken = "asd.as3g.egwg.dwq";

        // when, then
        Assertions.assertThatThrownBy(() ->
                        tokenService.decode(weirdToken))
                .as("토큰 형식이 맞지 않은 경우 예외를 발생한다.")
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ApiErrorCode.INVALIDED_AUTHORIZATION.name());

    }

}