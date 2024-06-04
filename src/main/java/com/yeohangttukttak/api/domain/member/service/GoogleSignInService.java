package com.yeohangttukttak.api.domain.member.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dao.RefreshTokenRepository;
import com.yeohangttukttak.api.domain.member.dto.MemberAuthDTO;
import com.yeohangttukttak.api.domain.member.entity.AuthType;
import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiRedirectException;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.time.Instant;
import java.util.Base64;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Transactional
public class GoogleSignInService {

    private final String CLIENT_SECRET, API_DOMAIN;

    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberRepository memberRepository;

    private final GoogleRevokeService revokeService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final RestClient client = RestClient.create();

    public GoogleSignInService(@Value("${GOOGLE_CLIENT_SECRET}") String CLIENT_SECRET,
                               @Value("${API_DOMAIN}") String API_DOMAIN,
                               RefreshTokenRepository refreshTokenRepository,
                               MemberRepository memberRepository,
                               GoogleRevokeService revokeService) {
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.API_DOMAIN = API_DOMAIN;
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
        this.revokeService = revokeService;
    }

    public MemberAuthDTO call(String code) throws JsonProcessingException {

        // 1. Google 인증 서버에 Token 교환 요청
        ResponseEntity<GoogleTokenDto> responseEntity = client.post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(APPLICATION_JSON)
                .body(new GoogleTokenIssueDto(CLIENT_SECRET, API_DOMAIN, code))
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
                            .authType(AuthType.GOOGLE)
                            .refreshToken(tokenDto.getRefreshToken())
                            .build();

                    memberRepository.save(newMember);
                    return newMember;
                });

        if (member.getAuthType() != AuthType.GOOGLE) {

            revokeService.call(tokenDto.getRefreshToken());

            throw new ApiRedirectException("com.yeohaeng.ttukttak.app:/",
                    ApiErrorCode.DUPLICATED_SOCIAL_EMAIL, member.getAuthType().getValue());
        }

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
        String redirectUri;

        @JsonProperty("grant_type")
        String grantType = "authorization_code";

        String code;

        public GoogleTokenIssueDto(String clientSecret, String apiDomain, String code) {
            this.clientSecret = clientSecret;
            this.code = code;
            this.redirectUri = apiDomain + "/api/v1/members/sign-in/google";
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
