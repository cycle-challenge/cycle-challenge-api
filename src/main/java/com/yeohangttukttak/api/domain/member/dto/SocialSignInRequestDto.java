package com.yeohangttukttak.api.domain.member.dto;

import com.yeohangttukttak.api.domain.member.entity.AgeGroup;
import com.yeohangttukttak.api.domain.member.entity.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SocialSignInRequestDto {

    private String nickname;

    private String email;

    private AgeGroup ageGroup;

    private Gender gender;

    private String idToken;
}
