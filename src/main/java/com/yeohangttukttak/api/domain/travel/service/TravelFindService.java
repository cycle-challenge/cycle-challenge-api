package com.yeohangttukttak.api.domain.travel.service;

import com.yeohangttukttak.api.domain.travel.dao.TravelRepository;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.visit.dao.VisitRepository;
import com.yeohangttukttak.api.domain.visit.dto.VisitDTO;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TravelFindService {

    private final TravelRepository travelRepository;

    private final VisitRepository visitRepository;

    public TravelDTO call(Long id) {

        Travel travel = travelRepository.find(id)
                .orElseThrow(() -> new ApiException(ApiErrorCode.TRAVEL_NOT_FOUND));

        List<VisitDTO> visits = visitRepository.findByTravel(id)
                .stream().map(VisitDTO::new)
                .toList();

        return new TravelDTO(travel);
    }

}
