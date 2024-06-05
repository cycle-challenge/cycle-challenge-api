package com.yeohangttukttak.api.domain.travel.service;

import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.Block;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.place.dao.PlaceReviewRepository;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.dto.PlaceReviewReportDto;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.visit.dao.VisitRepository;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.Comparator.*;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TravelFindNearbyService {

    private final VisitRepository visitRepository;
    private final PlaceReviewRepository placeReviewRepository;
    private final MemberRepository memberRepository;

    public List<TravelDTO> findNearby(Location location, int radius, String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        List<Member> blocks = member.getBlocks().stream().map(Block::getBlocked).toList();

        List<Visit> visits = visitRepository.findNearby(location, radius)
                .stream().filter((visit) -> !blocks.contains(visit.getTravel().getMember()))
                .toList();

        List<Long> placeIds = visits.stream()
                .map(Visit::getPlace)
                .distinct()
                .map(Place::getId).toList();

        Map<Long, PlaceReviewReportDto> reviewReportMap = placeReviewRepository.createReports(placeIds)
                .stream().collect(toMap(PlaceReviewReportDto::getPlaceId, identity()));

        Map<Travel, List<Place>> travelMap = visits.stream()
                .collect(groupingBy(Visit::getTravel,
                        mapping(Visit::getPlace, collectingAndThen(toSet(), ArrayList::new))));

        return travelMap.entrySet()
                .stream().map((entry) -> {

                    List<PlaceDTO> placeDTOS = entry.getValue().stream()
                            .map(place -> new PlaceDTO(place, reviewReportMap.get(place.getId())))
                            .toList();

                    return new TravelDTO(entry.getKey(), placeDTOS);
                })
                .sorted(comparingLong(TravelDTO::getId))
                .toList();
    }

}
