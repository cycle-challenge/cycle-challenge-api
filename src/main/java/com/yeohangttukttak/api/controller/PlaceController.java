package com.yeohangttukttak.api.controller;

import com.yeohangttukttak.api.domain.place.Location;
import com.yeohangttukttak.api.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceRepository placeRepository;

    @GetMapping
    public List<PlaceDTO> search(
            @RequestParam("location") Location location,
            @RequestParam("distance") @Range(min = 3000, max = 500000) int radius
    ) {
        return placeRepository.findByLocation(location, radius).stream()
                .map(PlaceDTO::new).toList();
    }

}
