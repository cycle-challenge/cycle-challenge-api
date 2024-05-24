package com.yeohangttukttak.api.domain.member.entity;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.member.api.MemberController;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Embedded
    private Password password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder
    public Member(Long id, String email, Password password,
                  String nickname, AuthType authType,
                  AgeGroup ageGroup, Gender gender) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.authType = authType;
        this.ageGroup = ageGroup;
        this.gender = gender;
    }

    public static Member fromProfile(MemberController.ProfileDto profileDto) {

        return Member.builder()
                .email(profileDto.getEmail())
                .nickname(profileDto.getNickname())
                .ageGroup(profileDto.getAgeGroup())
                .gender(profileDto.getGender())
                .authType(profileDto.getAuthType())
                .build();
    }

}
