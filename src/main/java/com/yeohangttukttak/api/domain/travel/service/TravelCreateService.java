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
public class TravelCreateService {

    private final TravelRepository travelRepository;
    private final MemberRepository memberRepository;

    public Long call(String email, String name, Visibility visibility) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ApiErrorCode.INVALID_AUTHORIZATION));

        Travel travel = Travel.builder().name(name)
                .member(member)
                .visibility(visibility)
                .build();

        return travelRepository.save(travel);
    }

}
