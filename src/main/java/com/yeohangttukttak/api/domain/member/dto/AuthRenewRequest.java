package com.yeohangttukttak.api.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthRenewRequest {

    private String refreshToken;

    private String email;

}
