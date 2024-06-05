package com.yeohangttukttak.api.domain.member.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yeohangttukttak.api.domain.member.entity.AgeGroup;
import com.yeohangttukttak.api.domain.member.entity.Member;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDTO {

    private Long id;

    private String nickname;

    private AgeGroup ageGroup;

    public MemberDTO (Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.ageGroup = member.getAgeGroup();
    }

}
