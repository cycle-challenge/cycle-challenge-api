package com.yeohangttukttak.api.domain.visit.api;

import com.yeohangttukttak.api.controller.api.ApiResponse;
import com.yeohangttukttak.api.domain.member.AgeGroup;
import com.yeohangttukttak.api.domain.place.dto.LocationDTO;
import com.yeohangttukttak.api.domain.travel.AccompanyType;
import com.yeohangttukttak.api.domain.travel.Motivation;
import com.yeohangttukttak.api.domain.travel.TransportType;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearch;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearchDTO;
import com.yeohangttukttak.api.domain.visit.service.VisitSearchService;
import com.yeohangttukttak.api.domain.place.Location;
import com.yeohangttukttak.api.validator.ValidValueBasedEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
@Slf4j
public class VisitController {

    private final VisitSearchService visitSearchService;


    @GetMapping("/search")
    public ApiResponse<VisitSearchDTO> search(
            @Valid @ModelAttribute VisitSearchParams params
    ) {
        Location location = new Location(
                params.getLocation().getLatitude(),
                params.getLocation().getLongitude());

        VisitSearch search = VisitSearch.builder()
                .location(location)
                .radius(params.getRadius())
                .accompanyType(params.getAccompanyType())
                .motivation(params.getMotivation())
                .ageGroup(params.getAgeGroup())
                .transportType(params.getTransportType())
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

        private AgeGroup ageGroup;

        private Motivation motivation;

        private AccompanyType accompanyType;

        private TransportType transportType;

    }
}
