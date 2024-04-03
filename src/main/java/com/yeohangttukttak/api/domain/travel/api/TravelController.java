package com.yeohangttukttak.api.domain.travel.api;

import com.yeohangttukttak.api.domain.travel.service.TravelFindVisitsService;
import com.yeohangttukttak.api.domain.travel.dto.TravelDaySummaryDTO;
import com.yeohangttukttak.api.global.common.ApiResponse;
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


    @GetMapping("/{id}/visits")
    ApiResponse<List<TravelDaySummaryDTO>> findVisits(@PathVariable("id") Long id) {
        return new ApiResponse<>(findVisitsService.find(id));
    }

}
