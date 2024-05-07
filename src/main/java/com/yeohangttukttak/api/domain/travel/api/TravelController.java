package com.yeohangttukttak.api.domain.travel.api;

import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.travel.dto.*;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.travel.service.*;
import com.yeohangttukttak.api.domain.visit.dto.VisitCreateDto;
import com.yeohangttukttak.api.domain.visit.dto.VisitDTO;
import com.yeohangttukttak.api.global.common.ApiCreatedResponse;
import com.yeohangttukttak.api.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/travels")
public class TravelController {
    private final TravelFindService findService;
    private final TravelFindNearbyService findNearbyService;
    private final TravelFindVisitsService findVisitsService;
    private final TravelFindBookmarkedService findBookmarkedService;
    private final TravelFindMyService findMyService;

    private final TravelCreateService createService;
    private final TravelCreateVisitsService createVisitsService;

    private final TravelModifyService modifyService;


    @PostMapping("/")
    public ResponseEntity<ApiCreatedResponse<Travel, Long>> create(
            HttpServletRequest request,
            @Valid @RequestBody TravelCreateDto body) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");

        Long id = createService.call(accessToken.getEmail(), body.getName(), body.getVisibility());

        return ResponseEntity.created(URI.create("/api/v1/travels/" + id))
                .body(new ApiCreatedResponse<>(Travel.class, id));
    }

    @GetMapping("/nearby")
    public ApiResponse<List<TravelDTO>> findNearby(
            @Valid @ModelAttribute TravelFindNearbyRequest params) {

        Location location = new Location(
                params.getLocation().getLatitude(),
                params.getLocation().getLongitude());
        int radius = params.getRadius();

        List<TravelDTO> travelDTOS = findNearbyService.findNearby(location, radius);
        return new ApiResponse<>(travelDTOS);
    }

    @GetMapping("/{id}")
    ApiResponse<TravelDTO> find(@PathVariable("id") Long id) {
        return new ApiResponse<>(findService.call(id));
    }

    @PostMapping("/{id}/visits")
    ResponseEntity<Void> createVisits(HttpServletRequest request,
                                      @PathVariable("id") Long id,
                                      @RequestBody List<VisitCreateDto> body) {

        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        createVisitsService.call(id, accessToken.getEmail(), body);

        return ResponseEntity.created(URI.create("/api/vi/travels/" + id + "/visits"))
                .build();
    }

    @GetMapping("/{id}/visits")
    ApiResponse<List<VisitDTO>> findVisits(@PathVariable("id") Long id) {
        return new ApiResponse<>(findVisitsService.call(id));
    }

    @GetMapping("/member/my")
    ApiResponse<List<TravelDTO>> findMy(HttpServletRequest request) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        return new ApiResponse<>(findMyService.call(accessToken.getEmail()));
    }

    @PatchMapping("/{id}")
    ResponseEntity<Void> modify(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            @Valid @RequestBody TravelModifyDto body) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        modifyService.call(id, accessToken.getEmail(), body);

        return ResponseEntity.noContent()
                .build();
    }


    @GetMapping("/bookmarked")
    public ApiResponse<List<TravelDTO>> findBookmarkedTravel(HttpServletRequest request) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        List<Travel> travels = findBookmarkedService.call(accessToken.getEmail());
        List<TravelDTO> dtos = travels.stream().map(TravelDTO::new).toList();

        return new ApiResponse<>(dtos);
    }

}
