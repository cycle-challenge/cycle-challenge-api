package com.yeohangttukttak.api.service.visit;

import com.yeohangttukttak.api.domain.place.Location;
import com.yeohangttukttak.api.domain.place.PlaceDTO;
import com.yeohangttukttak.api.domain.travel.TravelDTO;
import com.yeohangttukttak.api.domain.travel.Visit;
import com.yeohangttukttak.api.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .map(Visit::getPlace).distinct().map(PlaceDTO::new).toList();

        // travelDTO 조립
        List<TravelDTO> travelDTOS = visits.stream()
                .collect(toMap(
                    Visit::getTravel, Visit::getPlace,
                    (existValue, newValue) -> existValue
                ))
                .entrySet().stream()
                .map(entry -> new TravelDTO(entry.getKey(), entry.getValue()))
                .sorted(comparing(TravelDTO::getId))
                .toList();

        return new VisitSearchDTO(travelDTOS, placeDTOS);
    }

}
