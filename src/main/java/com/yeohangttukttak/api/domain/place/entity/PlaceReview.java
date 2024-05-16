package com.yeohangttukttak.api.domain.place.entity;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PlaceReview extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private Integer rating;

    private Boolean wantsToRevisit;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public PlaceReview(Integer rating, Boolean wantsToRevisit,
                       String comment, Place place, Member member) {
        this.rating = rating;
        this.wantsToRevisit = wantsToRevisit;
        this.comment = comment;
        this.place = place;
        this.member = member;
    }

}
