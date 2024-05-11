package com.yeohangttukttak.api.domain.travel.service;

import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.place.dao.PlaceRepository;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.travel.dao.TravelRepository;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.travel.entity.TravelPeriod;
import com.yeohangttukttak.api.domain.travel.entity.Visibility;
import com.yeohangttukttak.api.domain.visit.dao.VisitRepository;
import com.yeohangttukttak.api.domain.visit.dto.VisitCreateDto;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelCreateVisitsService {

    private final TravelRepository travelRepository;
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;

    public void call(Long travelId, String email, List<VisitCreateDto> createDtos) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ApiErrorCode.INVALID_AUTHORIZATION));

        Travel travel = travelRepository.find(travelId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.TRAVEL_NOT_FOUND));

        if (travel.getMember() != member) {
            throw new ApiException(ApiErrorCode.PERMISSION_DENIED);
        }

        // 모든 Place ID를 Set으로 수집
        Set<Long> placeIds = createDtos.stream()
                .map(VisitCreateDto::getPlaceId)
                .collect(Collectors.toSet());

        // 모든 Place를 Batch로 조회
        Map<Long, Place> placeMap = placeRepository.findAllById(placeIds)
                .stream()
                .collect(toMap(Place::getId, place -> place));

        List<Visit> visits = createDtos.stream().map((visit) -> {
            Place place = placeMap.get(visit.getPlaceId());

            if (place == null) {
                throw new ApiException(ApiErrorCode.PLACE_NOT_FOUND);
            }

            return Visit.builder().place(place).travel(travel)
                    .build();
        }).toList();

        travel.setVisits(visits);
        travelRepository.save(travel);
    }

}
