package com.yeohangttukttak.api.domain.place.dto;

import com.yeohangttukttak.api.domain.member.dto.MemberDTO;
import com.yeohangttukttak.api.domain.place.entity.PlaceReview;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlaceReviewDto {

    private Integer rating;

    private Boolean wantsToRevisit;

    private String comment;

    private MemberDTO member;

    private LocalDateTime createdAt;

    public PlaceReviewDto(PlaceReview review) {
        rating = review.getRating();
        wantsToRevisit = review.getWantsToRevisit();
        comment = review.getComment();
        member = new MemberDTO(review.getMember());
        createdAt = review.getCreatedAt();
    }
}
