package com.yeohangttukttak.api.controller;

import com.yeohangttukttak.api.domain.place.LocationDTO;
import com.yeohangttukttak.api.service.visit.VisitSearchDTO;
import com.yeohangttukttak.api.service.visit.VisitSearchService;
import com.yeohangttukttak.api.domain.place.Location;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitSearchService visitSearchService;


    @GetMapping("/search")
    public ApiResponse<VisitSearchDTO> search(
            @Valid @ModelAttribute VisitSearchParams params
    ) {
        VisitSearchDTO dto = visitSearchService.search(
                new Location(
                        params.location.getLatitude(),
                        params.location.getLongitude()),
                params.getRadius());

        return new ApiResponse<>(dto);
    }

    @Data
    static class VisitSearchParams {
        @Valid
        private LocationDTO location;

        @NotNull @Range(min = 3000, max = 50000)
        private Integer radius;
    }
}
