package com.yeohangttukttak.api.domain.place.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.place.entity.PlaceType;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.global.common.Reference;
import lombok.Data;

import java.util.*;

import static java.util.Comparator.comparing;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceDTO {

    private Long id;

    private String name;

    private LocationDTO location;

    private PlaceType type;

    private List<ImageDTO> images;

    private String googlePlaceId;

    private List<TravelDTO> travels;

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.type = place.getType();
        this.googlePlaceId = place.getGooglePlaceId();
        this.location = new LocationDTO(place.getLocation());
    }

    public PlaceDTO(Place place, List<TravelDTO> travels, List<ImageDTO> images, Double distance) {
        this(place);
        this.location = new LocationDTO(place.getLocation(), distance);
        this.travels = travels;
        this.images = images;
    }

}
