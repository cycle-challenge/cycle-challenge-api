package com.yeohangttukttak.api.domain.travel.entity;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.file.entity.File;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode
public class Travel extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private TravelPeriod period;

    @Enumerated(EnumType.STRING)
    private AccompanyType accompanyType;

    @Enumerated(EnumType.STRING)
    private TransportType transportType;

    @Enumerated(EnumType.STRING)
    private Motivation motivation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "travel")
    private List<Visit> visits = new ArrayList<>();

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "thumbnail_id")
    private File thumbnail;

    @Builder
    public Travel(Long id, String name,
                  TravelPeriod period,
                  AccompanyType accompanyType,
                  TransportType transportType,
                  Motivation motivation,
                  Member member) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.accompanyType = accompanyType;
        this.transportType = transportType;
        this.motivation = motivation;
        this.member = member;
    }

}
