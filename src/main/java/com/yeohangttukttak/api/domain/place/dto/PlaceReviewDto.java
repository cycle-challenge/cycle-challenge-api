package com.yeohangttukttak.api.domain.place.dto;

import com.yeohangttukttak.api.domain.place.entity.PlaceReview;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlaceReviewDto {

    private Integer rating;

    private Boolean wantsToRevisit;

    private String comment;

    private String nickname;

    private LocalDateTime createdAt;

    public PlaceReviewDto(PlaceReview review) {
        rating = review.getRating();
        wantsToRevisit = review.getWantsToRevisit();
        comment = review.getComment();
        nickname = review.getMember().getNickname();
        createdAt = review.getCreatedAt();
    }
}
