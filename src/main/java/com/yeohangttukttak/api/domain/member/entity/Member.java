package com.yeohangttukttak.api.domain.member.entity;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.bookmark.entity.PlaceBookmark;
import com.yeohangttukttak.api.domain.member.api.MemberController;
import com.yeohangttukttak.api.domain.member.dto.SocialSignInRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    private String nickname;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Builder
    public Member(Long id, String email, String nickname, AuthType authType, String refreshToken,
                  AgeGroup ageGroup) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.authType = authType;
        this.refreshToken = refreshToken;
        this.ageGroup = ageGroup;
    }

}
