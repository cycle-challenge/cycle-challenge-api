package com.yeohangttukttak.api.domain.travel.api;

import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.travel.service.TravelFindBookmarkedService;
import com.yeohangttukttak.api.domain.travel.service.TravelFindVisitsService;
import com.yeohangttukttak.api.domain.travel.dto.TravelDaySummaryDTO;
import com.yeohangttukttak.api.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/travels")
public class TravelController {

    private final TravelFindVisitsService findVisitsService;
    private final TravelFindBookmarkedService findBookmarkedService;

    @GetMapping("/{id}/visits")
    ApiResponse<List<TravelDaySummaryDTO>> findVisits(@PathVariable("id") Long id) {
        return new ApiResponse<>(findVisitsService.find(id));
    }

    @GetMapping("/bookmarked")
    public ApiResponse<List<TravelDTO>> findBookmarkedTravel(HttpServletRequest request) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        List<Travel> travels = findBookmarkedService.call(accessToken.getEmail());
        List<TravelDTO> dtos = travels.stream().map(TravelDTO::new).toList();

        return new ApiResponse<>(dtos);
    }

}
