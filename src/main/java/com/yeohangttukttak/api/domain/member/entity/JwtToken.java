package com.yeohangttukttak.api.domain.member.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeohangttukttak.api.domain.member.dto.TokenPayload;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

@Component
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtToken {

    private String token;

    private String email;

    private Long iat;

    private Long exp;

    protected static String SECRET;

    public static final Long accessTokenTTL = 60 * 30L,
            refreshTokenTTL = (60 * 60) * 24 * 14L;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JwtToken issueAccessToken(String email, Instant now) {
        return JwtToken.create(email, now, accessTokenTTL);
    }

    public static JwtToken issueRefreshToken(String email, Instant now) {
        return JwtToken.create(email, now, refreshTokenTTL);
    }

    private static JwtToken create(String email, Instant now, Long expSeconds) {
        try {

            TokenPayload payload = new TokenPayload(email, now, expSeconds);

            String content = String.format("%s.%s", serialize(new Header()), serialize(payload));
            String signature = signatureToken(content);

            String token = String.format("%s.%s", content, signature);

            return new JwtToken(token, payload.getEmail(), payload.getIat(), payload.getExp());

        } catch (NoSuchAlgorithmException | InvalidKeyException | JsonProcessingException e) {

            log.error("Decode Token:", e);
            throw new ApiException(ApiErrorCode.INTERNAL_SERVER_ERROR);

        }
    }

    public static JwtToken decode(String token) {
        try {
            String[] parts = token.split("\\.");

            // 1. 토큰의 형식을 검사
            if (parts.length != 3)
                throw new ApiException(ApiErrorCode.INVALID_AUTHORIZATION);

            String content = parts[0] + "." + parts[1];
            String signature = parts[2];

            // 2. 토큰 내용이 위조되었는지 서명을 대조
            if (!signatureToken(content).equals(signature))
                throw new ApiException(ApiErrorCode.INVALID_AUTHORIZATION);

            TokenPayload payload = deserialize(parts[1], TokenPayload.class);

            // 3. 토큰의 유효 기간을 검사
            Instant now = Instant.now();
            Instant expiration = Instant.ofEpochSecond(payload.getExp());

            if (now.isAfter(expiration))
                throw new ApiException(ApiErrorCode.INVALID_AUTHORIZATION);

            return new JwtToken(token, payload.getEmail(), payload.getIat(), payload.getExp());

        } catch (NoSuchAlgorithmException | InvalidKeyException | JsonProcessingException e) {
            log.error("Decode Token:", e);
            throw new ApiException(ApiErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    private static String signatureToken(String content) throws NoSuchAlgorithmException, InvalidKeyException {
        // Hmac SHA-256 알고리즘 가져오기
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");

        // 비밀 키를 사용해 SecretKeySpec 생성
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");

        hmacSha256.init(secretKeySpec);

        byte[] hash = hmacSha256.doFinal(content.getBytes());
        return toBase64Url(hash);
    }

    private static String serialize(Object object) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(object);
        return toBase64Url(json.getBytes());
    }

    private static <T> T deserialize(String hash, Class<T> type) throws JsonProcessingException {
        String json = fromBase64Url(hash);
        return objectMapper.readValue(json, type);
    }

    private static String toBase64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String fromBase64Url(String hash) {

        return new String(Base64.getUrlDecoder().decode(hash));
    }

    @Value("${secret.token}")
    public void setSECRET(String SECRET) {
        JwtToken.SECRET = SECRET;
    }

    @Getter
    private static class Header {

        String alg = "HS256";

        String type = "JWT";

    }
}
