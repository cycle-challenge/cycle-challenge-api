package com.yeohangttukttak.api.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeohangttukttak.api.domain.member.dto.TokenPayload;
import com.yeohangttukttak.api.domain.member.exception.TokenExpiredException;
import com.yeohangttukttak.api.domain.member.exception.TokenInvalidException;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

@Service
@Slf4j
public class TokenService {

    private final String SECRET;
    private final ObjectMapper objectMapper;

    public TokenService(
            @Value("${secret.token}") String secret,
            ObjectMapper objectMapper) {
        this.SECRET = secret;
        this.objectMapper = objectMapper;
    }

    public String issue(String email, Instant now, Long expSeconds){
        try {

            String header = serialize(new Header());
            String payload = serialize(new TokenPayload(email, now, expSeconds));

            String content = String.format("%s.%s", header, payload);
            String signature = signatureToken(content);

            return String.format("%s.%s", content, signature);

        } catch (NoSuchAlgorithmException | InvalidKeyException | JsonProcessingException e) {
            log.error("Decode Token:", e);
            throw new ApiException(ApiErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public TokenPayload decode(String token) {
        try {
            String[] tokens = token.split("\\.");

            // 1. 토큰의 형식을 검사
            if (tokens.length != 3)
                throw new TokenInvalidException();

            String content = tokens[0] + "." + tokens[1];
            String signature = tokens[2];

            // 2. 토큰 내용이 위조되었는지 서명을 대조
            if (!signatureToken(content).equals(signature))
                throw new TokenInvalidException();

            TokenPayload tokenPayload = deserialize(tokens[1], TokenPayload.class);

            // 3. 토큰의 유효 기간을 검사
            Instant now = Instant.now();
            Instant expiration = Instant.ofEpochSecond(tokenPayload.getExp());

            if (now.isAfter(expiration))
                throw new TokenExpiredException();

            return tokenPayload;

        } catch (NoSuchAlgorithmException | InvalidKeyException | JsonProcessingException e) {
            log.error("Decode Token:", e);
            throw new ApiException(ApiErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String signatureToken(String content) throws NoSuchAlgorithmException, InvalidKeyException {
        // Hmac SHA-256 알고리즘 가져오기
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");

        // 비밀 키를 사용해 SecretKeySpec 생성
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");

        hmacSha256.init(secretKeySpec);

        byte[] hash = hmacSha256.doFinal(content.getBytes());
        return toBase64Url(hash);
    }

    private String serialize(Object object) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(object);
        return toBase64Url(json.getBytes());
    }

    private <T> T deserialize(String hash, Class<T> type) throws JsonProcessingException {
        String json = fromBase64Url(hash);
        return objectMapper.readValue(json, type);
    }

    private String toBase64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String fromBase64Url(String hash) {

        return new String(Base64.getUrlDecoder().decode(hash));
    }

    @Getter
    private static class Header {

        String alg = "HS256";

        String type = "JWT";

    }

}
