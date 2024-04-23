package com.yeohangttukttak.api.domain.member.dto;

import lombok.Data;

@Data
public class MemberSignInRequest {

    private String email;

    private String password;

}
