package com.yeohangttukttak.api.domain.travel.service;

import com.yeohangttukttak.api.domain.travel.dao.TravelRepository;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.visit.dto.VisitDTO;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class TravelFindVisitsService {

    private final TravelRepository travelRepository;

    public List<VisitDTO> call(Long id) {
        Travel travel = travelRepository.find(id)
                .orElseThrow(() -> new ApiException(ApiErrorCode.TRAVEL_NOT_FOUND));

        return travel.getVisits().stream()
                .map(VisitDTO::new)
                .toList();
    }

}
