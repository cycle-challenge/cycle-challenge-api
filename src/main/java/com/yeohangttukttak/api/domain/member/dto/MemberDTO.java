package com.yeohangttukttak.api.domain.member.dto;
import com.yeohangttukttak.api.domain.member.entity.AgeGroup;
import com.yeohangttukttak.api.domain.member.entity.Member;
import lombok.Data;

@Data
public class MemberDTO {

    private String nickname;

    private AgeGroup ageGroup;

    public MemberDTO (Member member) {
        this.nickname = member.getNickname();
        this.ageGroup = member.getAgeGroup();
    }

}
