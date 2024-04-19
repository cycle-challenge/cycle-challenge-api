package com.yeohangttukttak.api.domain.member.api;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dto.*;
import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.domain.member.service.*;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import com.yeohangttukttak.api.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberSignInService signInService;
    private final MemberSignUpService signUpService;
    private final MemberAuthRenewService authRenewService;
    private final MemberFindProfileService findProfileService;

    private final MemberSendVerifyEmailService sendVerifyEmailService;

    private final MemberRepository memberRepository;

    @PostMapping("/email/verify/send")
    public void sendVerificationEmail(@Valid @RequestBody MemberSendVerifyEmailRequest body) {

        memberRepository.findByEmail(body.getEmail())
                .ifPresent(member -> {
                    throw new ApiException(ApiErrorCode.DUPLICATED_EMAIL);
                });

        sendVerifyEmailService.send(body.getEmail());
    }

    @GetMapping("/profile")
    public ApiResponse<MemberProfileDTO> findProfile(
            HttpServletRequest request, HttpServletResponse response
    ) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        return new ApiResponse<>(findProfileService.findByEmail(accessToken.getEmail()));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<MemberDTO>> signUp(@Valid @RequestBody MemberSignUpRequest body) {
        MemberDTO member = signUpService.local(
                body.getEmail(), body.getPassword(), body.getNickname(), body.getVerificationCode());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(member));
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

}
