package com.yeohangttukttak.api.domain.member.dto;

import com.yeohangttukttak.api.global.config.validator.ValidNickname;
import com.yeohangttukttak.api.global.config.validator.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberSignUpRequest {

    @NotBlank @Email
    private String email;

    @ValidPassword
    private String password;

    @ValidNickname
    private String nickname;

}
