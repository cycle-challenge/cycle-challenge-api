package com.yeohangttukttak.api.domain.member.dto;

import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import lombok.Data;

@Data
public class MemberAuthDTO {

    private String tokenType = "Bearer";

    private String accessToken;

    private String refreshToken;

    public MemberAuthDTO(JwtToken accessToken, JwtToken refreshToken) {
        this.accessToken = accessToken.getToken();
        this.refreshToken = refreshToken.getToken();
    }

}
