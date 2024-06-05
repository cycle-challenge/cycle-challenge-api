package com.yeohangttukttak.api.domain.travel.entity;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelReport extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "travel_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    private ReportReason reason;

    private String description;

    public TravelReport(Member member, Travel travel,
                        ReportReason reason, String description) {
        this.member = member;
        this.travel = travel;
        this.reason = reason;
        this.description = description;
    }

}
