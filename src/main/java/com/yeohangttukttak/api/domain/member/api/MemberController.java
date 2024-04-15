package com.yeohangttukttak.api.domain.member.api;
import com.yeohangttukttak.api.domain.member.dto.*;
import com.yeohangttukttak.api.domain.member.service.MemberAuthRenewService;
import com.yeohangttukttak.api.domain.member.service.MemberSignInService;
import com.yeohangttukttak.api.domain.member.service.MemberSignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public MemberAuthDTO signIn(@Valid @RequestBody MemberSignInRequest body) {
        return signInService.local(body.getEmail(), body.getPassword());
    }

    @PostMapping("/auth/renew")
    public MemberAuthDTO renew(@RequestBody MemberAuthRenewRequest body) {
        return memberAuthRenewService.renew(body.getRefreshToken(), body.getEmail());
    }

}
