package com.yeohangttukttak.api.service.visit;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.File;
import com.yeohangttukttak.api.domain.member.entity.AgeGroup;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.travel.entity.*;
import com.yeohangttukttak.api.domain.visit.dao.VisitRepository;
import com.yeohangttukttak.api.domain.visit.dao.VisitSearchResult;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearch;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearchDTO;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.domain.visit.service.VisitSearchService;
import com.yeohangttukttak.api.global.common.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VisitSearchServiceTest {

    @InjectMocks
    private VisitSearchService visitSearchService;

    @Mock
    private VisitRepository visitRepository;

    Member memberA, memberB;
    Travel travelA, travelB;
    Place placeA, placeB;
    Visit visitA, visitB, visitA2, visitB2;
    List<File> files;

    @BeforeEach
    public void init() {

        memberA = Member.builder()
                .id(1L)
                .ageGroup(AgeGroup.S20)
                .build();

        memberB = Member.builder()
                .id(2L)
                .ageGroup(AgeGroup.S30)
                .build();

        travelA = Travel.builder()
                .id(1L)
                .member(memberA)
                .accompanyType(AccompanyType.PARENTS)
                .motivation(Motivation.RELAX)
                .transportType(TransportType.CAR)
                .period(new TravelPeriod(
                        LocalDate.parse("2022-03-19"),
                        LocalDate.parse("2022-03-21")
                ))
                .build();

        travelB = Travel.builder()
                .id(2L)
                .member(memberB)
                .accompanyType(AccompanyType.CHILDREN)
                .motivation(Motivation.EDUCATION)
                .transportType(TransportType.PUBLIC)
                .period(new TravelPeriod(
                        LocalDate.parse("2022-08-15"),
                        LocalDate.parse("2022-08-17")
                ))
                .accompanyType(AccompanyType.FRIENDS)
                .build();

        placeA = Place.builder()
                .id(1L)
                .name("그랜드 플라자 청주 호텔")
                .location(new Location(36.6665, 127.4945))
                .build();

        placeB = Place.builder()
                .id(2L)
                .name("청주 시립 미술관")
                .location(new Location(36.6347, 127.4784))
                .build();

        visitA = Visit.builder()
                .id(1L).place(placeA).travel(travelA)
                .build();

        visitA2 = Visit.builder()
                .id(2L).place(placeB).travel(travelA)
                .build();

        visitB = Visit.builder()
                .id(3L).place(placeA).travel(travelB)
                .build();

        visitB2 = Visit.builder()
                .id(4L).place(placeB).travel(travelB)
                .build();

        files = new ArrayList<>(LongStream.range(1, 11)
                .mapToObj(id -> File.builder().id(id).build())
                .toList());

        Collections.shuffle(files);

        files.forEach(file -> placeA.getFiles().add(file));
    }


    private VisitSearchResult createResult(Visit visit) {
        return new VisitSearchResult(visit, visit.getTravel(), visit.getPlace(), 0.0);
    }

    private VisitSearchDTO performSearch() {
        VisitSearch search = new VisitSearch();
        List<VisitSearchResult> results = List.of(
                createResult(visitA2),
                createResult(visitB2),
                createResult(visitB),
                createResult(visitA)
        );;

        when(visitRepository.search(search)).thenReturn(results);

        return visitSearchService.search(search);
    }

    @Test
    public void 장소_중복() {
        VisitSearchDTO visitSearchDTO = performSearch();

        assertThat(visitSearchDTO.getPlaces())
                .as("장소는 ID 기준으로 중복되지 않아야 한다.")
                .hasSize(2);
    }

    @Test
    public void 장소_정렬() {
        VisitSearchDTO visitSearchDTO = performSearch();

        assertThat(visitSearchDTO.getPlaces())
                .extracting(PlaceDTO::getId)
                .as("반환된 장소는 ID 오름차순으로 정렬되어야 한다.")
                .containsExactly(1L, 2L);
    }

    @Test
    public void 장소_미리보기_사진() {
        VisitSearchDTO visitSearchDTO = performSearch();

        assertThat(visitSearchDTO.getPlaces().get(0).getImages())
                .as("장소는 상위 5개의 미리보기 사진을 반환해야 한다.")
                .hasSize(5)
                .extracting(ImageDTO::getId)
                .as("미리보기 사진은 ID 기준으로 오름차순으로 정렬되어야 한다.")
                .containsExactly(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    public void 여행_참조_정렬() {
        VisitSearchDTO visitSearchDTO = performSearch();
        List<Reference> travelRefs = List.of(
                new Reference(1L, "travel"),
                new Reference(2L, "travel"));

        assertThat(visitSearchDTO.getPlaces())
                .extracting(PlaceDTO::getTravels)
                .as("장소는 연관된 여행의 참조 데이터를 반환해야 한다.")
                .hasSize(2)
                .containsExactly(travelRefs, travelRefs);
    }

    @Test
    public void 장소_중복_검증() {
        VisitSearchDTO visitSearchDTO = performSearch();

        assertThat(visitSearchDTO.getTravels())
                .as("여행은 ID 기준으로 중복되지 않아야 한다.")
                .hasSize(2);
    }

    @Test
    public void 여행_정렬_검증() {
        VisitSearchDTO visitSearchDTO = performSearch();

        assertThat(visitSearchDTO.getTravels())
                .extracting(TravelDTO::getId)
                .as("반환된 여행은 ID 오름차순으로 정렬되어야 한다.")
                .containsExactly(1L, 2L);
    }


}
