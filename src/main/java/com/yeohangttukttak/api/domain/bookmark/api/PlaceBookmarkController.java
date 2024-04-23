package com.yeohangttukttak.api.domain.bookmark.api;

import com.yeohangttukttak.api.domain.bookmark.entity.PlaceBookmark;
import com.yeohangttukttak.api.domain.bookmark.service.PlaceBookmarkCreateService;
import com.yeohangttukttak.api.domain.bookmark.service.PlaceBookmarkDeleteService;
import com.yeohangttukttak.api.domain.bookmark.service.PlaceBookmarkFindService;
import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.place.service.PlaceGetPreviewImageService;
import com.yeohangttukttak.api.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class PlaceBookmarkController {

    private final PlaceBookmarkCreateService placeBookmarkCreateService;
    private final PlaceBookmarkDeleteService placeBookmarkDeleteService;
    private final PlaceBookmarkFindService placeBookmarkFindService;
    private final PlaceGetPreviewImageService getPreviewImageService;

    @PostMapping("/places/{id}")
    public ResponseEntity<ApiResponse<Void>> createPlaceBookmark(
            @PathVariable("id") Long id, HttpServletRequest request) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        placeBookmarkCreateService.call(accessToken.getEmail(), id);

        return ResponseEntity.created(URI.create("/api/v1/bookmarks/places"))
                .body(new ApiResponse<>(null));
    }

    @DeleteMapping("/places/{id}")
    public ApiResponse<Void> deletePlaceBookmark(
            @PathVariable("id") Long id, HttpServletRequest request) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        placeBookmarkDeleteService.call(accessToken.getEmail(), id);

        return new ApiResponse<>(null);
    }

    @GetMapping("/places/")
    public ApiResponse<List<PlaceDTO>> findPlaceBookmarks(HttpServletRequest request) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");

        List<PlaceBookmark> bookmarks = placeBookmarkFindService.call(accessToken.getEmail());
        List<Place> places = bookmarks.stream().map(PlaceBookmark::getTarget).toList();

        return new ApiResponse<>(getPreviewImageService.call(places));
    }

}
