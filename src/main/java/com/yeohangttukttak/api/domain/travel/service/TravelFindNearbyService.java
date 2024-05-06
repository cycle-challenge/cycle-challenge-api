package com.yeohangttukttak.api.domain.travel.service;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.dto.PlaceFindNearbyQueryDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.visit.dao.VisitRepository;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TravelFindNearbyService {

    private final VisitRepository visitRepository;

    public List<TravelDTO> findNearby(Location location, int radius) {

        List<Visit> visits = visitRepository.findNearby(location, radius);

        Map<Travel, List<Place>> travelMap = visits.stream()
                .collect(groupingBy(Visit::getTravel,
                        mapping(Visit::getPlace, toList())));

        Map<Long, List<ImageDTO>> imageMap = new HashMap<>();

        return travelMap.entrySet()
                .stream().map((entry) -> {
                    List<PlaceDTO> placeDTOS = entry.getValue().stream().map((place) -> {
                        if (!imageMap.containsKey(place.getId())) {
                            imageMap.put(place.getId(), getPreviewImage(place));
                        }
                        return new PlaceDTO(place, imageMap.get(place.getId()));
                    }).toList();

                    return new TravelDTO(entry.getKey(), placeDTOS);
                })
                .sorted(comparingLong(TravelDTO::getId))
                .toList();
    }


    private List<ImageDTO> getPreviewImage(Place place) {
        return place.getImages().stream()
                .sorted(comparingDouble(Image::getId))
                .limit(5)
                .map(ImageDTO::new).toList();
    }


}
