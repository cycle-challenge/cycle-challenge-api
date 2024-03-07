package com.yeohangttukttak.api.domain.place.dto;
import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.File;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.place.entity.PlaceType;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.visit.dao.VisitSearchResult;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.global.common.Reference;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.Map.Entry;

import static java.util.Comparator.comparing;


@Data
public class PlaceDTO {

    private Long id;

    private String name;

    private LocationDTO location;

    private PlaceType type;

    private List<ImageDTO> images;

    private List<Reference> travels;

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.type = place.getType();
        this.location = new LocationDTO(place.getLocation());
        this.images = place.getFiles().stream()
                .sorted(comparing(File::getId))
                .limit(5)
                .map(ImageDTO::new)
                .toList();
    }

    public PlaceDTO(Entry<Place, List<VisitSearchResult>> entry) {
        this(entry.getKey());
        List<VisitSearchResult> results = entry.getValue();

        location.setDistance(results.get(0).getDistance());

        travels = results.stream()
                .map(VisitSearchResult::getTravel)
                .map(result -> new Reference(result.getId(), "travel"))
                .distinct()
                .sorted(comparing(Reference::getId))
                .toList();
    }

}
