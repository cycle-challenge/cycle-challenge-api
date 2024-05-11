package com.yeohangttukttak.api.domain.place.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.place.entity.PlaceType;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import lombok.Data;

import java.util.*;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceDTO {

    private Long id;

    private String name;

    private LocationDTO location;

    private PlaceType type;

    private List<ImageDTO> images;

    private String googlePlaceId;

    private PlaceReviewReportDto review;

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.type = place.getType();
        this.googlePlaceId = place.getGooglePlaceId();
        this.location = new LocationDTO(place.getLocation());
        this.images = place.getPreviewImages().stream().map(ImageDTO::new).toList();
    }

    public PlaceDTO(Place place, PlaceReviewReportDto review) {
        this(place);
        this.review = review;
    }

}
