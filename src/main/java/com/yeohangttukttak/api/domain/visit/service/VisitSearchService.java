package com.yeohangttukttak.api.domain.visit.service;

import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearch;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearchDTO;
import com.yeohangttukttak.api.domain.visit.dao.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VisitSearchService {

    private final VisitRepository visitRepository;

    public VisitSearchDTO search(VisitSearch visitSearch) {
        List<Visit> visits = visitRepository.search(visitSearch);

        // place 탐색
        List<PlaceDTO> placeDTOS = visits.stream()
                .collect(groupingBy(Visit::getPlace,
                        mapping(Visit::getTravel, toList())))
                .entrySet().stream()
                .map(entry -> new PlaceDTO(entry.getKey(), entry.getValue()))
                .toList();

        // travelDTO 조립
        List<TravelDTO> travelDTOS = placeDTOS.stream()
                .map(PlaceDTO::getTravels)
                .flatMap(Collection::stream).distinct().toList();

        return new VisitSearchDTO(travelDTOS, placeDTOS);
    }

}
