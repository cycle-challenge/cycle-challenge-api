package com.yeohangttukttak.api.domain.travel.service;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.travel.dao.TravelRepository;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.travel.dto.TravelDayBoundDTO;
import com.yeohangttukttak.api.domain.travel.dto.TravelDaySummaryDTO;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.visit.dto.VisitDTO;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.domain.visit.dao.VisitRepository;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingDouble;

@RequiredArgsConstructor
@Service
public class TravelFindVisitsService {

    private final TravelRepository travelRepository;

    public List<VisitDTO> call(Long id) {
        Travel travel = travelRepository.find(id)
                .orElseThrow(() -> new ApiException(ApiErrorCode.TRAVEL_NOT_FOUND));

        return travel.getVisits()
                .stream().map((visit) -> new VisitDTO(visit,
                        new PlaceDTO(visit.getPlace(), getPreviewImage(visit.getPlace()))))
                .toList();
    }

    private List<ImageDTO> getPreviewImage(Place place) {
        return place.getImages().stream()
                .sorted(comparingDouble(Image::getId))
                .limit(5)
                .map(ImageDTO::new).toList();
    }


}
