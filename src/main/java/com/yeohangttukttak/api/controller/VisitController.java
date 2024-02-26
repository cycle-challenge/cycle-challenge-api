package com.yeohangttukttak.api.controller;

import com.yeohangttukttak.api.domain.place.Location;
import com.yeohangttukttak.api.domain.travel.Visit;
import com.yeohangttukttak.api.repository.VisitRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitRepository visitRepository;


    @GetMapping("/search")
    public ApiResponse<VisitSearchDTO> search(
            @Valid @ModelAttribute VisitSearchParams params
    ) {
        List<Visit> visits = visitRepository.findByLocation(
                params.getLocation(),
                params.getRadius());

        return new ApiResponse<>(new VisitSearchDTO(visits));
    }

    @Data
    static class VisitSearchParams {
        @NotNull
        Location location;

        @NotNull @Range(min = 3000, max = 50000, message = "값은 3000 ~ 50000 미터 이하여야 합니다.")
        int radius;
    }
}
