package com.yeohangttukttak.api.domain.place.api;

import com.yeohangttukttak.api.domain.place.dao.PlaceRepository;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.dto.PlaceFindNearbyParams;
import com.yeohangttukttak.api.domain.place.service.PlaceFindNearbyService;
import com.yeohangttukttak.api.global.common.ApiResponse;
import com.yeohangttukttak.api.domain.place.entity.Location;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.global.common.PageResult;
import com.yeohangttukttak.api.global.common.PageSearch;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceFindNearbyService placeFindNearbyService;
    private final PlaceRepository placeRepository;

    @GetMapping("/nearby")
    public ApiResponse<List<PlaceDTO>> findNearby(
            @Valid @ModelAttribute PlaceFindNearbyParams params
    ) {
        Location location = new Location(
                params.getLocation().getLatitude(),
                params.getLocation().getLongitude());
        int radius = params.getRadius();

        List<PlaceDTO> placeDTOS = placeFindNearbyService.findNearby(location, radius);
        return new ApiResponse<>(placeDTOS);
    }


    @GetMapping("/{id}/images")
    public ApiResponse<PageResult<ImageDTO>> getPlaceImages(
            @PathVariable("id") Long id,
            @ModelAttribute PageSearch search) {

        PageResult<Image> images = placeRepository.getImage(id, search);
        return new ApiResponse<>(images.convertEntities(ImageDTO::new));
    }

}
