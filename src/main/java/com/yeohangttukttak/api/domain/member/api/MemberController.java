package com.yeohangttukttak.api.domain.member.api;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dto.*;
import com.yeohangttukttak.api.domain.member.entity.*;
import com.yeohangttukttak.api.domain.member.service.*;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import com.yeohangttukttak.api.global.common.ApiResponse;
import io.netty.handler.codec.base64.Base64Decoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberAuthRenewService authRenewService;
    private final MemberFindProfileService findProfileService;

    private final GoogleSignInService googleSignInService;

    private final MemberRepository memberRepository;

    @GetMapping("/profile")
    public ApiResponse<MemberProfileDTO> findProfile(HttpServletRequest request) {

        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");

        return new ApiResponse<>(findProfileService.findByEmail(accessToken.getEmail()));

    }

    @PostMapping("/auth/renew")
    public ResponseEntity<ApiResponse<MemberAuthDTO>> renew(@RequestBody MemberAuthRenewRequest body) {
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(new ApiResponse<>(authRenewService.renew(body.getRefreshToken(), body.getEmail())));
    }

    @GetMapping("/sign-in/google")
    public ResponseEntity<Void> signInGoogle(@RequestParam("code") String code) throws JsonProcessingException {

        MemberAuthDTO authDTO = googleSignInService.call(code);

        // 앱의 Custom Scheme 으로 Redirection
        String redirectUri = UriComponentsBuilder.fromUriString("com.yeohaeng.ttukttak.app:/")
                .queryParam("access-token", authDTO.getAccessToken())
                .queryParam("refresh-token", authDTO.getRefreshToken())
                .encode().toUriString();

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUri)
                .build();
    }

    @Transactional
    @DeleteMapping("/")
    public ApiResponse<Void> deleteUser(HttpServletRequest request) {

        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");

        Member member = memberRepository.findByEmail(accessToken.getEmail())
                .orElseThrow(() -> new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        memberRepository.delete(member);

        return new ApiResponse<>(null);

    }

}
