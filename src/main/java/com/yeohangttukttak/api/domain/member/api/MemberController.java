package com.yeohangttukttak.api.domain.member.api;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dto.*;
import com.yeohangttukttak.api.domain.member.entity.*;
import com.yeohangttukttak.api.domain.member.service.*;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import com.yeohangttukttak.api.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberAuthRenewService authRenewService;
    private final MemberFindProfileService findProfileService;

    private final GoogleSignInService googleSignInService;
    private final GoogleRevokeService googleRevokeService;

    private final AppleSignInService appleSignInService;

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
                .queryParam("status", "success")
                .queryParam("access-token", authDTO.getAccessToken())
                .queryParam("refresh-token", authDTO.getRefreshToken())
                .encode().toUriString();

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUri)
                .build();
    }

    @Transactional
    @PostMapping("/sign-in/apple")
    public ResponseEntity<Void> signInApple(@RequestParam("code") String code,
                                            @RequestParam(value = "user", required = false) String user) throws JsonProcessingException {


        MemberAuthDTO authDTO = appleSignInService.call(code, user);

        // 앱의 Custom Scheme 으로 Redirection
        String redirectUri = UriComponentsBuilder.fromUriString("com.yeohaeng.ttukttak.app:/")
                .queryParam("status", "success")
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

        googleRevokeService.call(member);

        return new ApiResponse<>(null);

    }

}
