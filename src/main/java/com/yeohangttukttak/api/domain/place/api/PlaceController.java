package com.yeohangttukttak.api.domain.place.api;

import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.domain.place.dao.PlaceRepository;
import com.yeohangttukttak.api.domain.place.dao.PlaceReviewRepository;
import com.yeohangttukttak.api.domain.place.dao.PlaceSuggestionRepository;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.dto.PlaceReviewCreateDto;
import com.yeohangttukttak.api.domain.place.dto.PlaceReviewDto;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.place.entity.PlaceReview;
import com.yeohangttukttak.api.domain.place.entity.PlaceSuggestion;
import com.yeohangttukttak.api.domain.place.service.PlaceFindBookmarkedService;
import com.yeohangttukttak.api.global.common.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.Image;

import static java.lang.String.format;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceReviewRepository placeReviewRepository;
    private final PlaceSuggestionRepository placeSuggestionRepository;
    private final PlaceRepository placeRepository;
    private final PlaceFindBookmarkedService placeBookmarkFindService;

    @PostMapping("/{id}/comments")
    public ResponseEntity<Void> createComment(
            @PathVariable("id") Long placeId,
            @RequestBody PlaceReviewCreateDto body) {

        placeRepository.find(placeId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.PLACE_NOT_FOUND));

        String id = placeReviewRepository.insert(
                new PlaceReview(placeId, body.getRating(), body.getComment()));

        URI entityUri = URI.create(format("/api/v1/places/%d/comments/%s", placeId, id));

        return ResponseEntity.created(entityUri).build();
    }

    @GetMapping("/{id}/comments")
    public ApiResponse<List<PlaceReviewDto>> findComments(
            @PathVariable("id") Long placeId) {

        placeRepository.find(placeId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.PLACE_NOT_FOUND));

        List<PlaceReviewDto> reviews = placeReviewRepository
                .findByPlace(placeId).stream().map(PlaceReviewDto::new).toList();

        return new ApiResponse<>(reviews);

    }

    @GetMapping("/autocomplete")
    public ApiResponse<List<PlaceSuggestion>> autoComplete(@RequestParam("query") String query) {
        return new ApiResponse<>(placeSuggestionRepository.search(query));
    }

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

    @GetMapping("/{id}")
    public ApiResponse<PlaceDTO> find(@PathVariable("id") Long id) {
        Place place = placeRepository.find(id)
                .orElseThrow(() -> new ApiException(ApiErrorCode.PLACE_NOT_FOUND));

        return new ApiResponse<>(new PlaceDTO(place));
    }

}
