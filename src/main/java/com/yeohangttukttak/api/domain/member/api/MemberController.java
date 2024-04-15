package com.yeohangttukttak.api.domain.member.api;
import com.yeohangttukttak.api.domain.member.dto.*;
import com.yeohangttukttak.api.domain.member.service.AuthRenewService;
import com.yeohangttukttak.api.domain.member.service.MemberSignInService;
import com.yeohangttukttak.api.domain.member.service.MemberSignUpService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberSignInService signInService;
    private final MemberSignUpService signUpService;
    private final AuthRenewService authRenewService;

    @PostMapping("/sign-up")
    public void signUp(@Valid @RequestBody SignUpRequest body) {
        signUpService.local(body.getEmail(), body.getPassword(), body.getNickname());
    }

    @PostMapping("/sign-in")
    public SignInDTO signIn(@Valid @RequestBody SignInRequest body) {
        return signInService.local(body.getEmail(), body.getPassword());
    }

    @PostMapping("/renew")
    public SignInDTO renew(@RequestBody AuthRenewRequest body) {
        return authRenewService.renew(body.getRefreshToken(), body.getEmail());
    }

}
