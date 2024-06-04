package com.yeohangttukttak.api.domain.place.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;

@Data
@AllArgsConstructor
public class PlaceReviewReportDto {

    private Long placeId;

    private Long count;

    private Double ratingAvg;

}
