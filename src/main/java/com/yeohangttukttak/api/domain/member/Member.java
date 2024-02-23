package com.yeohangttukttak.api.domain.member;

import com.yeohangttukttak.api.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED) @Getter
class Member extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

}
