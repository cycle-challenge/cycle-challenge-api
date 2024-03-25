package com.yeohangttukttak.api.domain.place.dto;
import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.place.entity.PlaceType;
import com.yeohangttukttak.api.global.common.Reference;
import lombok.Data;

import java.util.*;

import static java.util.Comparator.comparing;


@Data
public class PlaceDTO {

    private Long id;

    private String name;

    private LocationDTO location;

    private PlaceType type;

    private List<ImageDTO> images;

    private String googlePlaceId;

    private int visitCount;

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.type = place.getType();
        this.googlePlaceId = place.getGooglePlaceId();
        this.location = new LocationDTO(place.getLocation());
    }

    public PlaceDTO(Place place, List<ImageDTO> imageDTOS, Double distance, int visitCount) {
        this(place);

        this.getLocation().setDistance(distance);
        this.images = imageDTOS;
        this.visitCount = visitCount;
    }

}
