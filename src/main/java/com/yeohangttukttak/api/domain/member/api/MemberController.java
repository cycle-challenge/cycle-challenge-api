package com.yeohangttukttak.api.domain.member.api;
import com.nimbusds.jose.JOSEException;
import com.yeohangttukttak.api.domain.member.dto.*;
import com.yeohangttukttak.api.domain.member.entity.*;
import com.yeohangttukttak.api.domain.member.service.*;
import com.yeohangttukttak.api.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberAuthRenewService authRenewService;
    private final MemberFindProfileService findProfileService;

    private final GoogleSignInService googleSignInService;

    @GetMapping("/profile")
    public ApiResponse<MemberProfileDTO> findProfile(
            HttpServletRequest request, HttpServletResponse response
    ) {
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

    @Transactional
    @PostMapping("/sign-in/google")
    public ApiResponse< MemberAuthDTO> googleSignIn(@RequestBody SocialSignInRequestDto body) throws IOException, NoSuchAlgorithmException, InvalidKeyException, JOSEException, SignatureException {

        MemberAuthDTO authDTO = googleSignInService.call(body);

        return new ApiResponse<>(authDTO);

    }

}
