package com.yeohangttukttak.api.domain.place.service;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.entity.Place;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.toMap;

@Service
public class PlaceGetPreviewImageService {

    public List<PlaceDTO> call(List<Place> places) {
        Map<Long, List<ImageDTO>> imageMap = places.stream().collect(toMap(Place::getId,
                place -> place.getImages().stream()
                        .sorted(comparingDouble(Image::getId))
                        .limit(5)
                        .map(ImageDTO::new).toList()));

        return places.stream().map((place) -> new PlaceDTO(place, imageMap.get(place.getId()))).toList();
    }

}
