package com.yeohangttukttak.api.domain.visit.service;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.Image;
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

    public List<TravelDTO> search(VisitSearch search) {
        List<VisitSearchResult> results = visitRepository.search(search);

        Map<Long, List<PlaceDTO>> map = new HashMap<>();

        // place 탐색
        results.stream()
                .collect(groupingBy(
                        VisitSearchResult::getPlace,
                        mapping(identity(), toList()
                        )))
                .forEach((key, value) -> {
                    PlaceDTO placeDTO = new PlaceDTO(key,
                            getPreviewImages(value),
                            getDistance(value),
                            value.size());
                    value.forEach(e -> {
                        Long travelId = e.getTravel().getId();

                        if (!map.containsKey(travelId))
                            map.put(travelId, new ArrayList<>());

                        map.get(travelId).add(placeDTO);
                    });
                });


        // travelDTO 조립
        return results.stream()
                .map(VisitSearchResult::getTravel)
                .map(travel -> new TravelDTO(travel,
                        map.get(travel.getId()), getThumbnail(travel)))
                .distinct()
                .sorted(comparing(TravelDTO::getId))
                .toList();
    }

    private Double getDistance(List<VisitSearchResult> results) {
        return results.get(0).getDistance();
    }

    private List<ImageDTO> getPreviewImages(List<VisitSearchResult> results) {
        return results.stream()
                .map(VisitSearchResult::getVisit)
                .map(Visit::getImages)
                .flatMap(Collection::stream)
                .sorted(comparing(Image::getId))
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
        return travel.getThumbnail() == null ? null : new ImageDTO(travel.getThumbnail());
    }


}
