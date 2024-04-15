package com.yeohangttukttak.api.domain.member.api;
import com.yeohangttukttak.api.domain.member.dto.*;
import com.yeohangttukttak.api.domain.member.service.MemberAuthRenewService;
import com.yeohangttukttak.api.domain.member.service.MemberSignInService;
import com.yeohangttukttak.api.domain.member.service.MemberSignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberSignInService signInService;
    private final MemberSignUpService signUpService;
    private final MemberAuthRenewService memberAuthRenewService;

    @PostMapping("/sign-up")
    public void signUp(@Valid @RequestBody MemberSignUpRequest body) {
        signUpService.local(body.getEmail(), body.getPassword(), body.getNickname());
    }

    @PostMapping("/sign-in")
    public ResponseEntity<MemberAuthDTO> signIn(@Valid @RequestBody MemberSignInRequest body) {
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(signInService.local(body.getEmail(), body.getPassword()));
    }

    @PostMapping("/auth/renew")
    public ResponseEntity<MemberAuthDTO> renew(@RequestBody MemberAuthRenewRequest body) {
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(memberAuthRenewService.renew(body.getRefreshToken(), body.getEmail()));
    }

}
