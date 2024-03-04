package com.yeohangttukttak.api.domain.place.dto;
import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.place.entity.PlaceType;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import lombok.Data;

import java.util.List;

@Data
public class PlaceDTO {

    private Long id;

    private String name;

    private LocationDTO location;

    private PlaceType type;

    private List<ImageDTO> images;

    private List<TravelDTO> travels;

    public PlaceDTO(Place place, List<Travel> travels) {
        this.id = place.getId();
        this.name = place.getName();
        this.type = place.getType();
        this.location = new LocationDTO(new Location(place.getPoint()));
        this.images = place.getFiles().stream().map(ImageDTO::new).toList();
        this.travels = travels.stream().map(TravelDTO::new).toList();
    }
}
