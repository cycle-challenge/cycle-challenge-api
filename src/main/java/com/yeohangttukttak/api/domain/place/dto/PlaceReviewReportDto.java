package com.yeohangttukttak.api.domain.place.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;

@Data
@AllArgsConstructor
public class PlaceReviewReportDto {

    private Long placeId;

    private Integer count;

    private Double ratingAvg;

    public PlaceReviewReportDto(Document document) {
        this.placeId = document.getLong("_id");
        this.count = document.getInteger("count");
        this.ratingAvg = document.getDouble("ratingAvg");
    }
}
