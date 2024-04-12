package com.yeohangttukttak.api.domain.member.entity;

import com.yeohangttukttak.api.domain.BaseEntity;
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
}
