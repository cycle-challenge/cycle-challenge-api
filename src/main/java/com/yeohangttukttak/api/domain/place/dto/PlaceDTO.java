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

    private List<TravelDTO> travels;

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.type = place.getType();
        this.googlePlaceId = place.getGooglePlaceId();
        this.location = new LocationDTO(place.getLocation());
    }

    public PlaceDTO(Place place, List<ImageDTO> images) {
        this(place);
        this.images = images;
    }

    public PlaceDTO(Place place, List<ImageDTO> images, List<TravelDTO> travels) {
        this(place, images);
        this.travels = travels;
    }

    public PlaceDTO(Place place, List<TravelDTO> travels, List<ImageDTO> images, Double distance) {
        this(place, images, travels);
        this.location = new LocationDTO(place.getLocation(), distance);
    }

}
