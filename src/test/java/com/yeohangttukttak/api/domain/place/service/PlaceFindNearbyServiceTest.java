package com.yeohangttukttak.api.domain.place.service;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.domain.member.entity.AgeGroup;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.place.dao.PlaceRepository;
import com.yeohangttukttak.api.domain.place.dto.PlaceFindNearbyQueryDTO;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.travel.entity.*;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
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
public class PlaceFindNearbyServiceTest {

    @InjectMocks
    private PlaceFindNearbyService placeFindNearbyService;

    @Mock
    private PlaceRepository placeRepository;

    Member memberA, memberB;
    Travel travelA, travelB;
    Place placeA, placeB;
    Visit visitA, visitB, visitB2;
    List<Image> images;

    Location location = new Location(36.6600, 127.4900);
    int radius = 3000;

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
                .thumbnail(Image.builder().build())
                .build();

        travelB = Travel.builder()
                .id(2L)
                .member(memberB)
                .accompanyType(AccompanyType.CHILDREN)
                .motivation(Motivation.EDU)
                .transportType(TransportType.PUBLIC)
                .period(new TravelPeriod(
                        LocalDate.parse("2022-08-15"),
                        LocalDate.parse("2022-08-17")
                ))
                .accompanyType(AccompanyType.FRIENDS)
                .thumbnail(Image.builder().build())
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

        visitB = Visit.builder()
                .id(3L).place(placeB).travel(travelB)
                .build();

        visitB2 = Visit.builder()
                .id(2L).place(placeB).travel(travelA)
                .build();

        images = new ArrayList<>(LongStream.range(1, 11)
                .mapToObj(id -> Image.builder().id(id).build())
                .toList());

        Collections.shuffle(images);
        images.forEach(file -> placeA.getImages().add(file));

        Collections.shuffle(images);
        images.forEach(file -> placeB.getImages().add(file));

    }


    @Test
    public void 장소_인기순_정렬() {
        // given
        when(placeRepository.findNearby(location, radius)).thenReturn(List.of(
                new PlaceFindNearbyQueryDTO(placeA, 826.0),
                new PlaceFindNearbyQueryDTO(placeB, 2997.0)
        ));

        // when
        List<PlaceDTO> results = placeFindNearbyService.findNearby(location, radius);

        // then
        assertThat(results)
                .extracting(PlaceDTO::getId)
                .as("장소 목록은 관련된 여행의 개수가 큰 순으로 정렬되어야 한다.")
                .containsExactly(2L, 1L);
    }

    @Test
    public void 장소_미리보기_사진() {
        // given
        when(placeRepository.findNearby(location, radius)).thenReturn(List.of(
                new PlaceFindNearbyQueryDTO(placeA, 826.0)
        ));

        // when
        List<PlaceDTO> results = placeFindNearbyService.findNearby(location, radius);

        // then
        assertThat(results.get(0).getImages())
                .as("장소는 5개의 미리보기 사진을 반환해야 한다.")
                .hasSize(5)
                .extracting(ImageDTO::getId)
                .as("미리보기 사진은 ID 기준으로 오름차순으로 정렬되어야 한다.")
                .containsExactly(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    public void 여행_정렬() {
        // given
        when(placeRepository.findNearby(location, radius)).thenReturn(List.of(
                new PlaceFindNearbyQueryDTO(placeB, 2997.0)
        ));

        // when
        List<PlaceDTO> results = placeFindNearbyService.findNearby(location, radius);

        // then
        assertThat(results.get(0).getTravels())
                .as("장소는 연관된 여행 목록을 모두 반환해야 한다.")
                .hasSize(2)
                .extracting(TravelDTO::getId)
                .as("장소는 ID 오름차순 기준으로 정렬되어야 한다.")
                .containsExactly(1L, 2L);
    }

}
