package com.yeohangttukttak.api.domain.member;
import com.yeohangttukttak.api.domain.member.AgeGroup;
import com.yeohangttukttak.api.domain.member.Member;
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
