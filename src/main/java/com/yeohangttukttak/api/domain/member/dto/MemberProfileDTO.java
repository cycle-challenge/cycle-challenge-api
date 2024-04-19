package com.yeohangttukttak.api.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yeohangttukttak.api.domain.member.entity.Member;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberProfileDTO {

    private String email;

    private String nickname;

    public MemberProfileDTO (Member member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }

}
