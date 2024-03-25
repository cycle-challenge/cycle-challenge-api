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
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.global.common.PageResult;
import com.yeohangttukttak.api.global.common.PageSearch;
import lombok.AllArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
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

    @GetMapping("/{id}/images")
    public ApiResponse<PlaceImageDTO> getPlaceImages(
            @PathVariable("id") Long id,
            @ModelAttribute PageSearch search) {

        PageResult<Image> images = placeRepository.getPlaceImage(id, search);

        return new ApiResponse<>(new PlaceImageDTO(images.convertEntities(ImageDTO::new)));

    }

    @Data
    @AllArgsConstructor
    public static class PlaceImageDTO {

        PageResult<ImageDTO> images;

    }
}
