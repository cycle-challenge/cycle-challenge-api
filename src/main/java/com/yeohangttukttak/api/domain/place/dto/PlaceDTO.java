package com.yeohangttukttak.api.domain.place.dto;
import com.yeohangttukttak.api.domain.file.ImageDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.place.entity.PlaceType;
import lombok.Data;

import java.util.List;

@Data
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
