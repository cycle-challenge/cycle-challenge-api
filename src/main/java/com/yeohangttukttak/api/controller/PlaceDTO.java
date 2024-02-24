package com.yeohangttukttak.api.controller;
import com.yeohangttukttak.api.domain.place.Location;
import com.yeohangttukttak.api.domain.place.Place;
import com.yeohangttukttak.api.domain.place.PlaceType;
import lombok.Getter;

import java.util.List;

@Getter
public class PlaceDTO {

    private Long id;

    private String name;

    private LocationDTO location;

    private PlaceType type;

    private List<ImageDTO> images;

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.type = place.getType();
        this.location = new LocationDTO(new Location(place.getPoint()));
        this.images = place.getFiles().stream().map(ImageDTO::new).toList();
    }
}
