package com.yeohangttukttak.api.domain.visit.service;

import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.visit.dao.VisitSearchResult;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearch;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearchDTO;
import com.yeohangttukttak.api.domain.visit.dao.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VisitSearchService {

    private final VisitRepository visitRepository;

    public VisitSearchDTO search(VisitSearch search) {
        List<VisitSearchResult> results = visitRepository.search(search);

        // place 탐색
        List<PlaceDTO> placeDTOS = results.stream()
                .collect(groupingBy(
                        VisitSearchResult::getPlace,
                            mapping(identity(), toList()
                        )))
                .entrySet().stream()
                .map(PlaceDTO::new)
                .sorted(comparing(PlaceDTO::getId))
                .toList();

        // travelDTO 조립
        List<TravelDTO> travelDTOS = results.stream()
                .map(VisitSearchResult::getTravel)
                .map(TravelDTO::new)
                .distinct()
                .sorted(comparing(TravelDTO::getId))
                .toList();

        return new VisitSearchDTO(travelDTOS, placeDTOS);
    }




}
