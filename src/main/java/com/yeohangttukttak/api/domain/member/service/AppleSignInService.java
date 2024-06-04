package com.yeohangttukttak.api.domain.member.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dao.RefreshTokenRepository;
import com.yeohangttukttak.api.domain.member.dto.MemberAuthDTO;
import com.yeohangttukttak.api.domain.member.entity.AuthType;
import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.member.entity.NicknameGenerator;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiRedirectException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.time.Instant;
import java.util.Base64;

@Service
@Transactional
public class AppleSignInService {

    private final String CLIENT_SECRET, API_DOMAIN;

    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberRepository memberRepository;

    private final AppleRevokeService revokeService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final RestClient client = RestClient.create();

    public AppleSignInService(@Value("${APPLE_CLIENT_SECRET}") String CLIENT_SECRET,
                              @Value("${API_DOMAIN}") String API_DOMAIN,
                              RefreshTokenRepository refreshTokenRepository,
                              MemberRepository memberRepository,
                              AppleRevokeService revokeService) {
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.API_DOMAIN = API_DOMAIN;
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
        this.revokeService = revokeService;
    }

    public MemberAuthDTO call(String code) throws JsonProcessingException {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("code", code);
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", API_DOMAIN + "/api/v1/members/sign-in/apple");
        body.add("client_id", "app.yeohaeng.ttukttak.com");
        body.add("client_secret", CLIENT_SECRET);

        ResponseEntity<OAuthDto> response = client.post()
                .uri(URI.create("https://appleid.apple.com/auth/token"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(OAuthDto.class);

        OAuthDto oauthDto = response.getBody();

        // 3. ID 토큰 Payload Parsing
        String[] split = oauthDto.idToken.split("\\.");

        String jsonPayload = new String(Base64.getUrlDecoder().decode(split[1]));
        IdTokenPayload payload = objectMapper.readValue(jsonPayload, IdTokenPayload.class);

        // 4. ID 토큰의 정보로 회원가입 / 로그인
        Member member = memberRepository.findByEmail(payload.getEmail())
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .email(payload.getEmail())
                            .nickname(NicknameGenerator.random())
                            .refreshToken(oauthDto.getRefreshToken())
                            .authType(AuthType.APPLE)
                            .build();

                    memberRepository.save(newMember);
                    return newMember;
                });

        if (member.getAuthType() != AuthType.APPLE) {

            revokeService.call(oauthDto.getRefreshToken());

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

    }

    @Data
    public static class AppleSignInCallbackDto {

        private String code;

        @JsonProperty("id_token")
        private String idToken;

        private String user;

    }
    @Data
    public static class OAuthDto {

        @JsonProperty("id_token")
        String idToken;

        @JsonProperty("refresh_token")
        String refreshToken;

    }

}
