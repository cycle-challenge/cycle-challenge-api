package com.yeohangttukttak.api.domain.place.api;

import com.yeohangttukttak.api.domain.place.dao.PlaceRepository;
import com.yeohangttukttak.api.domain.place.dto.FindPlaceNearbyQueryDTO;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.global.common.ApiResponse;
import com.yeohangttukttak.api.domain.place.dto.LocationDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
@Slf4j
public class PlaceController {

    private final PlaceRepository placeRepository;


    @GetMapping("/nearby")
    public ApiResponse<List<PlaceDTO>> findNearby(
            @Valid @ModelAttribute PlaceFindNearbyParams params
    ) {
        Location location = new Location(
                params.getLocation().getLatitude(),
                params.getLocation().getLongitude());
        int radius = params.getRadius();

        List<FindPlaceNearbyQueryDTO> results = placeRepository
                .findNearby(location, radius);
        List<PlaceDTO> placeDTOS = results.stream().map(PlaceDTO::new).toList();

        return new ApiResponse<>(placeDTOS);
    }

    @Data
    static class PlaceFindNearbyParams {

        @Valid
        private LocationDTO location;

        @NotNull @Range(min = 3000, max = 50000)
        private Integer radius;

    }
}
