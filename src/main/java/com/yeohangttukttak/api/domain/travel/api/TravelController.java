package com.yeohangttukttak.api.domain.travel.api;

import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.travel.dao.TravelRepository;
import com.yeohangttukttak.api.domain.travel.dto.*;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.travel.service.*;
import com.yeohangttukttak.api.domain.visit.dao.VisitRepository;
import com.yeohangttukttak.api.domain.visit.dto.VisitCreateDto;
import com.yeohangttukttak.api.domain.visit.dto.VisitDTO;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.global.common.ApiCreatedResponse;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import com.yeohangttukttak.api.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    private final TravelRepository travelRepository;
    private final VisitRepository visitRepository;

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
            HttpServletRequest request,
            @Valid @ModelAttribute TravelFindNearbyRequest params) {

        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");

        Location location = new Location(
                params.getLocation().getLatitude(),
                params.getLocation().getLongitude());
        int radius = params.getRadius();

        List<TravelDTO> travelDTOS = findNearbyService.findNearby(location, radius, accessToken.getEmail());
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


    @DeleteMapping("/{id}/visits/{visitId}")
    @Transactional
    public ApiResponse<Void> deleteVisit(
            HttpServletRequest request,
            @PathVariable("id") Long id,
            @PathVariable("visitId") Long visitId) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");

        Travel travel = travelRepository.find(id).orElseThrow(
                () -> new ApiException(ApiErrorCode.TRAVEL_NOT_FOUND));

        String email = travel.getMember().getEmail();

        if (!email.equals(accessToken.getEmail())) {
            throw new ApiException(ApiErrorCode.PERMISSION_DENIED);
        }

        Visit visit = visitRepository.find(visitId).orElseThrow(
                () -> new ApiException(ApiErrorCode.VISIT_NOT_FOUND));

        visitRepository.delete(visit);

        return new ApiResponse<>(null);
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
