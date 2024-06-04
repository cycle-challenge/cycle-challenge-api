package com.yeohangttukttak.api.domain.place.dto;

import lombok.Data;

@Data
public class PlaceReviewCreateDto {

    private Integer rating;

    private Boolean wantsToRevisit;

    private String comment;

}
