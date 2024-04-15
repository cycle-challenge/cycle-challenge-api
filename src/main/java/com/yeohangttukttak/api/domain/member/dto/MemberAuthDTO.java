package com.yeohangttukttak.api.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberAuthDTO {

    private String accessToken;

    private String refreshToken;

}
