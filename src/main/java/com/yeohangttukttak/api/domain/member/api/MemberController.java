package com.yeohangttukttak.api.domain.member.api;
import com.yeohangttukttak.api.domain.member.dto.SignUpRequest;
import com.yeohangttukttak.api.domain.member.service.MemberSignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberSignUpService signUpService;

    @PostMapping("/sign-up")
    public void signUp(@Valid @RequestBody SignUpRequest body) {
        signUpService.local(body.getEmail(), body.getPassword(), body.getNickname());
    }

}
