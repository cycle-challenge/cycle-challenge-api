package com.yeohangttukttak.api.domain.visit.api;

import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.travel.entity.Season;
import com.yeohangttukttak.api.global.common.ApiResponse;
import com.yeohangttukttak.api.domain.member.entity.AgeGroup;
import com.yeohangttukttak.api.domain.place.dto.LocationDTO;
import com.yeohangttukttak.api.domain.travel.entity.AccompanyType;
import com.yeohangttukttak.api.domain.travel.entity.Motivation;
import com.yeohangttukttak.api.domain.travel.entity.TransportType;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearch;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearchDTO;
import com.yeohangttukttak.api.domain.visit.service.VisitSearchService;
import com.yeohangttukttak.api.domain.place.entity.Location;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
@Slf4j
public class VisitController {

    private final VisitSearchService visitSearchService;


    @GetMapping("/search")
    public ApiResponse<List<TravelDTO>> search(
            @Valid @ModelAttribute VisitSearchParams params
    ) {
        Location location = new Location(
                params.getLocation().getLatitude(),
                params.getLocation().getLongitude());

        VisitSearch search = VisitSearch.builder()
                .location(location)
                .radius(params.getRadius())
                .accompanyTypes(params.getAccompanyTypes())
                .motivations(params.getMotivations())
                .ageGroups(params.getAgeGroups())
                .transportTypes(params.getTransportTypes())
                .seasons(params.getSeasons())
                .build();

        log.info(search.toString());

        return new ApiResponse<>(visitSearchService.search(search));
    }

    @Data
    static class VisitSearchParams {
        @Valid
        private LocationDTO location;

        @NotNull @Range(min = 3000, max = 50000)
        private Integer radius;

        private Set<AgeGroup> ageGroups;

        private Set<Motivation> motivations;

        private Set<AccompanyType> accompanyTypes;

        private Set<TransportType> transportTypes;

        private Set<Season> seasons;

    }
}
