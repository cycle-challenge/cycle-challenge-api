package com.yeohangttukttak.api.domain.member.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.yeohangttukttak.api.domain.member.api.MemberController;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dao.RefreshTokenRepository;
import com.yeohangttukttak.api.domain.member.dto.MemberAuthDTO;
import com.yeohangttukttak.api.domain.member.dto.SocialSignInRequestDto;
import com.yeohangttukttak.api.domain.member.dto.TokenHeader;
import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.text.ParseException;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Transactional
public class GoogleSignInService {

    private final String GOOGLE_CLIENT_SECRET;

    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberRepository memberRepository;

    public GoogleSignInService(@Value("${GOOGLE_CLIENT_SECRET}") String GOOGLE_CLIENT_SECRET,
            RefreshTokenRepository refreshTokenRepository,
            MemberRepository memberRepository) {
        this.GOOGLE_CLIENT_SECRET = GOOGLE_CLIENT_SECRET;
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final RestClient client = RestClient.create();

    public MemberAuthDTO call(String code) throws JsonProcessingException {

        // 1. Google 인증 서버에 Token 교환 요청
        ResponseEntity<GoogleTokenDto> responseEntity = client.post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(APPLICATION_JSON)
                .body(new GoogleTokenIssueDto(GOOGLE_CLIENT_SECRET, code))
                .retrieve()
                .toEntity(GoogleTokenDto.class);

        // 2. 인증 서버에서 받은 Token 저장 및 회원가입/로그인 처리
        GoogleTokenDto tokenDto = responseEntity.getBody();

        // 3. ID 토큰 Payload Parsing
        String[] split = tokenDto.idToken.split("\\.");

        String jsonPayload = new String(Base64.getUrlDecoder().decode(split[1]));
        IdTokenPayload payload = objectMapper.readValue(jsonPayload, IdTokenPayload.class);

        // 4. ID 토큰의 정보로 회원가입 / 로그인
        Member member = memberRepository.findByEmail(payload.getEmail())
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .email(payload.getEmail())
                            .nickname(payload.getName())
                            .refreshToken(tokenDto.getRefreshToken())
                            .build();

                    memberRepository.save(newMember);
                    return newMember;
                });

        // 5. 서비스 Token 발급하기
        Instant now = Instant.now();

        JwtToken accessToken = JwtToken.issueAccessToken(member.getEmail(), now);
        JwtToken refreshToken = JwtToken.issueRefreshToken(member.getEmail(), now);

        refreshTokenRepository.save(member.getId(), refreshToken.getToken(), JwtToken.refreshTokenTTL);

        return new MemberAuthDTO(accessToken, refreshToken);

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IdTokenPayload {

        private String email;

        private String name;

    }

    @NoArgsConstructor
    @Getter
    public static class GoogleTokenIssueDto {

        @JsonProperty("client_id")
        String clientId = "951324022006-eigc5h6tj71rm2v31eqr5u0v07cbmpn1.apps.googleusercontent.com";

        @JsonProperty("client_secret")
        String clientSecret;

        @JsonProperty("redirect_uri")
        String redirectUri = "http://172.30.1.25.nip.io:8080/api/v1/members/sign-in/google";

        @JsonProperty("grant_type")
        String grantType = "authorization_code";

        String code;

        public GoogleTokenIssueDto(String clientSecret, String code) {
            this.clientSecret = clientSecret;
            this.code = code;
        }
    }

    @Data
    @NoArgsConstructor
    public static class GoogleTokenDto {

        @JsonProperty("id_token")
        String idToken;

        @JsonProperty("refresh_token")
        String refreshToken;

    }

}
