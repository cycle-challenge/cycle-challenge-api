package com.yeohangttukttak.api.domain.place.service;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.domain.place.dao.PlaceRepository;
import com.yeohangttukttak.api.domain.place.dto.FindPlaceNearbyQueryDTO;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public List<PlaceDTO> findNearby(Location location, int radius) {
        List<FindPlaceNearbyQueryDTO> results = placeRepository
                .findNearby(location, radius);

        Map<Long, List<ImageDTO>> imageMap = createPreviewImageMap(
                results.stream()
                        .map(FindPlaceNearbyQueryDTO::getPlace)
                        .toList());

        return results.stream()
                .map(result -> new PlaceDTO(result.getPlace(),
                        imageMap.get(result.getPlace().getId()),
                        result.getDistance()))
                .toList();
    }


    Map<Long, List<ImageDTO>> createPreviewImageMap(List<Place> places) {
        return places.stream().collect(toMap(Place::getId,
                    place -> place.getImages().stream()
                        .sorted(comparingDouble(Image::getId))
                        .limit(5)
                        .map(ImageDTO::new).toList()));
    }

}
