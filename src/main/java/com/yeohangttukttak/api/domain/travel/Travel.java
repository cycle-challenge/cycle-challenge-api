package com.yeohangttukttak.api.domain.travel;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.file.File;
import com.yeohangttukttak.api.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
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

    @Builder
    public Travel(String name,
                  TravelPeriod period,
                  AccompanyType accompanyType,
                  TransportType transportType,
                  Motivation motivation) {

        this.name = name;
        this.period = period;
        this.accompanyType = accompanyType;
        this.transportType = transportType;
        this.motivation = motivation;
    }

}
