package com.yeohangttukttak.api.domain.member.entity;

import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Base64;


class JwtTokenTest {

    @BeforeAll
    static void init() {
        JwtToken jwtToken = new JwtToken();
        jwtToken.setSECRET("TEST");
    }


    @Test
    public void 발급_해독_정상_케이스() throws Exception {
        // given
        String email = "heebeom@example.com";
        Instant now = Instant.now();

        // when
        JwtToken accessToken = JwtToken.issueAccessToken(email, now);
        JwtToken decodedToken = JwtToken.decode(accessToken.getToken());

        // then
        Assertions.assertThat(decodedToken.getEmail())
                .as("해독한 AccessToken의 내용이 달라지지 않아야 한다")
                .isEqualTo(email);
    }

    @Test
    public void 토큰_유효기간_만료_케이스() {
        // given
        String email = "heebeom@example.com";

        // 발급 시간을 TTL 2배 전으로 설정해 바로 만료되도록 설정한다.
        Instant now = Instant.now().minusSeconds(JwtToken.accessTokenTTL * 2);

        // when, then
        JwtToken accessToken = JwtToken.issueAccessToken(email, now);

        Assertions.assertThatThrownBy(() ->
                        JwtToken.decode(accessToken.getToken()))
                .as("토큰의 만료 시간이 지난 경우 예외를 발생 한다.")
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ApiErrorCode.AUTHORIZATION_EXPIRED.name());
    }

    @Test
    public void 토큰_서명_위조_케이스() throws Exception {
        // given
        String email = "heebeom@example.com";
        Instant now = Instant.now();

        JwtToken accessToken = JwtToken.issueAccessToken(email, now);

        // 토큰의 내용을 위조 한다. (Email을 변경)
        String[] parts = accessToken.getToken().split("\\.");
        String fakePayload = Base64.getEncoder().encodeToString("{\"email\":\"heejin@exmple.com\"}".getBytes());
        String fakeToken = parts[0] + "." + fakePayload + "." + parts[2];

        // when, then
        Assertions.assertThatThrownBy(() ->
                        JwtToken.decode(fakeToken))
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
                        JwtToken.decode(weirdToken))
                .as("토큰 형식이 맞지 않은 경우 예외를 발생한다.")
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ApiErrorCode.INVALIDED_AUTHORIZATION.name());

    }
}