package com.yeohangttukttak.api.domain.place.dto;

import com.yeohangttukttak.api.domain.place.entity.PlaceReview;
import lombok.Data;

@Data
public class PlaceReviewDto {

    private Integer rating;

    private String comment;

    public PlaceReviewDto(PlaceReview review) {
        rating = review.getRating();
        comment = review.getComment();
    }
}
