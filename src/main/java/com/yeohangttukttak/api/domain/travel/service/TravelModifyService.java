package com.yeohangttukttak.api.domain.travel.service;

import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.travel.dao.TravelRepository;
import com.yeohangttukttak.api.domain.travel.dto.TravelModifyDto;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.travel.entity.TravelPeriod;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelModifyService {

    private final MemberRepository memberRepository;
    private final TravelRepository travelRepository;

    public void call(Long id, String email, TravelModifyDto modifyDto) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ApiErrorCode.INVALID_AUTHORIZATION));

        Travel travel = travelRepository.find(id)
                .orElseThrow(() -> new ApiException(ApiErrorCode.TRAVEL_NOT_FOUND));

        if (travel.getMember() != member) {
            throw new ApiException(ApiErrorCode.PERMISSION_DENIED);
        }

        travel.setName(modifyDto.getName());

        if (modifyDto.getStartedOn() != null && modifyDto.getEndedOn() != null) {
            travel.setPeriod(new TravelPeriod(modifyDto.getStartedOn(), modifyDto.getEndedOn()));
        }

        travel.setVisibility(modifyDto.getVisibility());

        Map<Long, Visit> visitMap = travel.getVisits().stream()
                .collect(Collectors.toMap(Visit::getId, Function.identity()));

        List<Visit> visits = modifyDto.getVisits().stream()
                .map((visitModifyDto) -> {
                    Visit visit = visitMap.get(visitModifyDto.getId());

                    if (visit == null) {
                        throw new ApiException(ApiErrorCode.VISIT_NOT_FOUND);
                    }

                    visit.setOrderOfVisit(visitModifyDto.getOrderOfVisit());
                    visit.setDayOfTravel(visitModifyDto.getDayOfTravel());

                    return visit;
                }).toList();

        travel.setVisits(visits);
        travelRepository.save(travel);
    }


}
