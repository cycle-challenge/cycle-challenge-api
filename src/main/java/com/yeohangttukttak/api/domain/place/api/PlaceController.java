package com.yeohangttukttak.api.domain.place.api;

import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.domain.place.dao.PlaceRepository;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.place.service.PlaceFindBookmarkedService;
import com.yeohangttukttak.api.domain.place.service.PlaceFindByGooglePlaceIdService;
import com.yeohangttukttak.api.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
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

    private final PlaceRepository placeRepository;
    private final PlaceFindBookmarkedService placeBookmarkFindService;
    private final PlaceFindByGooglePlaceIdService findByGooglePlaceIdService;

    @GetMapping("/{id}/images")
    public ApiResponse<PageResult<ImageDTO>> getPlaceImages(
            @PathVariable("id") Long id,
            @ModelAttribute PageSearch search) {

        PageResult<Image> images = placeRepository.getImage(id, search);
        return new ApiResponse<>(images.convertEntities(ImageDTO::new));
    }

    @GetMapping("/bookmarked")
    public ApiResponse<List<PlaceDTO>> findBookmarkedPlace(HttpServletRequest request) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        List<Place> places = placeBookmarkFindService.call(accessToken.getEmail());
        return new ApiResponse<>(places.stream().map(PlaceDTO::new).toList());
    }

    @GetMapping("/")
    public ApiResponse<PlaceDTO> findByGooglePlaceId(
            @RequestParam("googlePlaceId") String googlePlaceId) {
        Place place = findByGooglePlaceIdService.call(googlePlaceId);
        return new ApiResponse<>(new PlaceDTO(place));
    }

}
