package com.yeohangttukttak.api.domain.member.api;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dao.RefreshTokenRepository;
import com.yeohangttukttak.api.domain.member.dto.*;
import com.yeohangttukttak.api.domain.member.entity.*;
import com.yeohangttukttak.api.domain.member.service.*;
import com.yeohangttukttak.api.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberSignInService signInService;
    private final MemberSignUpService signUpService;
    private final MemberAuthRenewService authRenewService;
    private final MemberFindProfileService findProfileService;

    private final MemberSendVerifyEmailService sendVerifyEmailService;
    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberRepository memberRepository;


    @PostMapping("/email/verify/send")
    public ApiResponse<Void> sendVerificationEmail(@Valid @RequestBody MemberSendVerifyEmailRequest body) {

        sendVerifyEmailService.send(body.getEmail());

        return new ApiResponse<>(null);
    }

    @GetMapping("/profile")
    public ApiResponse<MemberProfileDTO> findProfile(
            HttpServletRequest request, HttpServletResponse response
    ) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        return new ApiResponse<>(findProfileService.findByEmail(accessToken.getEmail()));
    }

    @PostMapping("/sign-up")
    public ApiResponse<MemberDTO> signUp(@Valid @RequestBody MemberSignUpRequest body) {
        MemberDTO member = signUpService.local(
                body.getEmail(), body.getPassword(), body.getNickname(), body.getVerificationCode());

        return new ApiResponse<>(member);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<MemberAuthDTO>> signIn(@Valid @RequestBody MemberSignInRequest body) {
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(new ApiResponse<>(signInService.local(body.getEmail(), body.getPassword())));
    }

    @PostMapping("/auth/renew")
    public ResponseEntity<ApiResponse<MemberAuthDTO>> renew(@RequestBody MemberAuthRenewRequest body) {
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(new ApiResponse<>(authRenewService.renew(body.getRefreshToken(), body.getEmail())));
    }

    @Transactional
    @GetMapping("/sign-in/google/callback")
    public ApiResponse< MemberAuthDTO> googleSignInCallback(
            @RequestParam("clientId") String clientId,
            @RequestParam("code") String code,
            @RequestParam("codeVerifier") String codeVerifier) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<GoogleAccessTokenDto> response = restTemplate.postForEntity(
                URI.create("https://oauth2.googleapis.com/token"),
                new GoogleAccessCreateDto(code, codeVerifier, clientId),
                GoogleAccessTokenDto.class);

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                    .scheme("https").host("people.googleapis.com/v1/people/me")
                    .queryParam("personFields", "emailAddresses,birthdays,genders,names,photos")
                    .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + response.getBody().getAccessToken());

        ResponseEntity<JsonNode> res = restTemplate.exchange(
                uriComponents.toUri(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                JsonNode.class
        );

        ProfileDto profile = ProfileDto.parse(res.getBody());
        Member member = memberRepository.findByEmail(profile.getEmail())
                .orElseGet(() -> {
                    Member newMember = Member.fromProfile(profile);
                    memberRepository.save(newMember);

                    return newMember;
                });

        Instant now = Instant.now();
        JwtToken accessToken = JwtToken.issueAccessToken(profile.getEmail(), now);
        JwtToken refreshToken = JwtToken.issueRefreshToken(profile.getEmail(), now);

        refreshTokenRepository.save(member.getId(), refreshToken.getToken(), JwtToken.refreshTokenTTL);

        return new ApiResponse<>(new MemberAuthDTO(accessToken, refreshToken));
    }

    @Data
    @AllArgsConstructor
    public static class ProfileDto {

        private String nickname;

        private String email;

        private AgeGroup ageGroup;

        private Gender gender;

        private AuthType authType;

        public static ProfileDto parse (JsonNode jsonNode) {

            JsonNode birthdayNode = jsonNode.get("birthdays").get(0).get("date");

            LocalDate birthDate = LocalDate.of(
                    birthdayNode.get("year").asInt(),
                    birthdayNode.get("month").asInt(),
                    birthdayNode.get("day").asInt());

            JsonNode genderNode = jsonNode.get("genders").get(0).get("value");

            return new ProfileDto(
                    jsonNode.get("names").get(0).get("displayName").asText(),
                    jsonNode.get("emailAddresses").get(0).get("value").asText(),
                    AgeGroup.parseFromBirthDate(birthDate),
                    Gender.fromString(genderNode.asText()),
                    AuthType.GOOGLE);
        }

    }


    @Data
    public static class GoogleAccessCreateDto {

        String code;

        @JsonProperty("code_verifier")
        String codeVerifier;

        @JsonProperty("client_id")
        String clientId;

        @JsonProperty("grant_type")
        String grantType = "authorization_code";

        @JsonProperty("redirect_uri")
        String redirectUri;

        public GoogleAccessCreateDto(String code, String codeVerifier, String clientId) {
            this.code = code;
            this.codeVerifier = codeVerifier;
            this.clientId = String.format("%s.apps.googleusercontent.com", clientId);
            this.redirectUri = String.format("com.googleusercontent.apps.%s:/oauthredirect", clientId);
        }
    }

    @Data
    public static class GoogleAccessTokenDto {

        @JsonProperty("access_token")
        String accessToken;

        @JsonProperty("refresh_token")
        String refreshToken;

        String scope;

    }

}
