package com.yeohangttukttak.api.domain.travel.service;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.domain.place.dao.PlaceReviewRepository;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.dto.PlaceFindNearbyQueryDTO;
import com.yeohangttukttak.api.domain.place.dto.PlaceReviewDto;
import com.yeohangttukttak.api.domain.place.dto.PlaceReviewReportDto;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.visit.dao.VisitRepository;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.*;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TravelFindNearbyService {

    private final VisitRepository visitRepository;
    private final PlaceReviewRepository placeReviewRepository;

    public List<TravelDTO> findNearby(Location location, int radius) {

        List<Visit> visits = visitRepository.findNearby(location, radius);

        List<Long> placeIds = visits.stream()
                .map(Visit::getPlace)
                .distinct()
                .map(Place::getId).toList();

        Map<Long, PlaceReviewReportDto> reviewReportMap = placeReviewRepository.createReports(placeIds)
                .stream().collect(toMap(PlaceReviewReportDto::getPlaceId, identity()));

        Map<Travel, List<Place>> travelMap = visits.stream()
                .collect(groupingBy(Visit::getTravel,
                        mapping(Visit::getPlace, collectingAndThen(toSet(), ArrayList::new))));

        return travelMap.entrySet()
                .stream().map((entry) -> {

                    List<PlaceDTO> placeDTOS = entry.getValue().stream()
                            .map(place -> new PlaceDTO(place, reviewReportMap.get(place.getId())))
                            .toList();

                    return new TravelDTO(entry.getKey(), placeDTOS);
                })
                .sorted(comparingLong(TravelDTO::getId))
                .toList();
    }

}
