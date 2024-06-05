package com.yeohangttukttak.api.domain.place.entity;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.travel.entity.ReportReason;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceReviewReport extends BaseEntity  {

    @Id @GeneratedValue
    @Column(name = "place_review_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_reivew_id")
    private PlaceReview review;

    private ReportReason reason;

    private String description;

    public PlaceReviewReport(Member member, PlaceReview review,
                             ReportReason reason, String description) {
        this.member = member;
        this.review = review;
        this.reason = reason;
        this.description = description;
    }

}
