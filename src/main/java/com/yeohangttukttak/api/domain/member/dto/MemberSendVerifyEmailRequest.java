package com.yeohangttukttak.api.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberSendVerifyEmailRequest {

    @NotBlank @Email
    private String email;

}
