package com.yeohangttukttak.api.domain.visit.service;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.File;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.visit.dao.VisitSearchResult;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearch;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearchDTO;
import com.yeohangttukttak.api.domain.visit.dao.VisitRepository;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.global.common.Reference;
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
                .map(entry -> new PlaceDTO(entry.getKey(),
                        getTravelRef(entry.getValue()),
                        getPreviewImages(entry.getValue()),
                        getDistance(entry.getValue()))
                )
                .sorted(comparing(PlaceDTO::getId))
                .toList();

        // travelDTO 조립
        List<TravelDTO> travelDTOS = results.stream()
                .map(VisitSearchResult::getTravel)
                .map(travel -> new TravelDTO(travel, getThumbnail(travel)))
                .distinct()
                .sorted(comparing(TravelDTO::getId))
                .toList();

        return new VisitSearchDTO(travelDTOS, placeDTOS);
    }

    private Double getDistance(List<VisitSearchResult> results) {
        return results.get(0).getDistance();
    }

    private List<ImageDTO> getPreviewImages(List<VisitSearchResult> results) {
        return results.stream()
                .map(VisitSearchResult::getVisit)
                .map(Visit::getFiles)
                .flatMap(Collection::stream)
                .sorted(comparing(File::getId))
                .limit(5)
                .map(ImageDTO::new)
                .toList();
    }

    private List<Reference> getTravelRef(List<VisitSearchResult> results) {
        return results.stream()
                .map(VisitSearchResult::getTravel)
                .map(result -> new Reference(result.getId(), "travel"))
                .distinct()
                .sorted(comparing(Reference::getId))
                .toList();
    }

    private ImageDTO getThumbnail(Travel travel) {
        File thumbnail = travel.getFiles().stream().findFirst().orElse(null);
        return thumbnail == null ? null : new ImageDTO(thumbnail);
    }


}
