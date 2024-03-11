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

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder
    public Member(Long id, String nickname, AgeGroup ageGroup, Gender gender) {
        this.id = id;
        this.nickname = nickname;
        this.ageGroup = ageGroup;
        this.gender = gender;
    }
}
