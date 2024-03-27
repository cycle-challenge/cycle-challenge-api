package com.yeohangttukttak.api.domain.place.service;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.domain.place.dao.PlaceRepository;
import com.yeohangttukttak.api.domain.place.dto.PlaceFindNearbyQueryDTO;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class PlaceFindNearbyService {

    private final PlaceRepository placeRepository;

    public List<PlaceDTO> findNearby(Location location, int radius) {
        List<PlaceFindNearbyQueryDTO> results = placeRepository
                .findNearby(location, radius);

        Map<Long, List<ImageDTO>> imageMap = createPreviewImageMap(
                results.stream()
                        .map(PlaceFindNearbyQueryDTO::getPlace)
                        .toList());

        return results.stream()
                .map(result -> new PlaceDTO(result.getPlace(),
                            getOrderedTravels(result.getPlace()),
                            imageMap.get(result.getPlace().getId()),
                            result.getDistance()))
                .sorted(comparingInt((PlaceDTO dto) -> dto.getTravels().size()).reversed())
                .toList();
    }


    private Map<Long, List<ImageDTO>> createPreviewImageMap(List<Place> places) {
        return places.stream().collect(toMap(Place::getId,
                    place -> place.getImages().stream()
                        .sorted(comparingDouble(Image::getId))
                        .limit(5)
                        .map(ImageDTO::new).toList()));
    }

    private List<TravelDTO> getOrderedTravels(Place place) {
        return place.getVisits().stream()
                .map(Visit::getTravel)
                .sorted(comparingLong(Travel::getId))
                .map(TravelDTO::new).toList();
    }


}
