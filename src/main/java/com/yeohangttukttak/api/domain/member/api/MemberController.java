package com.yeohangttukttak.api.domain.member.api;
import com.yeohangttukttak.api.domain.member.dto.SignInDTO;
import com.yeohangttukttak.api.domain.member.dto.SignInRequest;
import com.yeohangttukttak.api.domain.member.dto.SignUpRequest;
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

    @PostMapping("/sign-up")
    public void signUp(@Valid @RequestBody SignUpRequest body) {
        signUpService.local(body.getEmail(), body.getPassword(), body.getNickname());
    }

    @PostMapping("/sign-in")
    public SignInDTO signIn(@Valid @RequestBody SignInRequest body) {
        return signInService.local(body.getEmail(), body.getPassword());
    }

}
