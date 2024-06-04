package com.yeohangttukttak.api.domain.bookmark.api;

import com.yeohangttukttak.api.domain.bookmark.dto.BookmarkDTO;
import com.yeohangttukttak.api.domain.bookmark.service.PlaceBookmarkCreateService;
import com.yeohangttukttak.api.domain.bookmark.service.PlaceBookmarkDeleteService;
import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class PlaceBookmarkController {

    private final PlaceBookmarkCreateService bookmarkCreateService;
    private final PlaceBookmarkDeleteService bookmarkDeleteService;

    @PostMapping("/places/{id}")
    public ResponseEntity<ApiResponse<BookmarkDTO>> createPlaceBookmark(
            @PathVariable("id") Long id, HttpServletRequest request) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        BookmarkDTO dto = bookmarkCreateService.call(accessToken.getEmail(), id);

        return ResponseEntity.created(URI.create("/api/v1/places/" + dto.getTargetId()))
                .body(new ApiResponse<>(dto));
    }

    @DeleteMapping("/places/{id}")
    public ApiResponse<BookmarkDTO> deletePlaceBookmark(
            @PathVariable("id") Long id, HttpServletRequest request) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        BookmarkDTO dto = bookmarkDeleteService.call(accessToken.getEmail(), id);

        return new ApiResponse<>(dto);
    }

}
