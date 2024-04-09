package com.yeohangttukttak.api.domain.travel.service;

import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.domain.place.dto.LocationDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.place.entity.PlaceType;
import com.yeohangttukttak.api.domain.travel.dto.BoundDTO;
import com.yeohangttukttak.api.domain.travel.dto.TravelDaySummaryDTO;
import com.yeohangttukttak.api.domain.travel.entity.*;
import com.yeohangttukttak.api.domain.visit.dto.VisitDTO;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.domain.visit.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelFindVisitsServiceTest {

    @InjectMocks
    private TravelFindVisitsService travelFindVisitsService;

    @Mock
    private VisitRepository visitRepository;

    Travel travel;
    List<Visit> visits;
    List<Image> images;

    @BeforeEach
    public void init() {

        travel = Travel.builder().id(1L).build();

        // Initialize Visits
        visits = List.of(
                createVisit(1L, 2, 2, "그랜드 플라자 청주 호텔", PlaceType.HOTEL, 33.0, 126.0),
                createVisit(2L, 1, 2, "스누피가든", PlaceType.NATURE, 33.01, 126.01),
                createVisit(3L, 3, 1, "청주 시립 미술관", PlaceType.CULTURE, 32.0, 125.0),
                createVisit(4L, 2, 1, "섭지코지", PlaceType.NATURE, 36.0, 136.0)
        );

        // Initialize Images and shuffle for each visit
        images = new ArrayList<>(LongStream.range(1, 11)
                .mapToObj(id -> Image.builder().id(id).build())
                .toList());

        visits.forEach(visit -> {
            Collections.shuffle(images);
            images.forEach(image -> visit.getImages().add(image));
        });

        when(visitRepository.findByTravel(1L)).thenReturn(visits);

    }

    @Test
    public void 방문_일자_그룹화() throws Exception {
        // when
        List<TravelDaySummaryDTO> result = travelFindVisitsService.find(1L);

        // then
        assertThat(result)
                .extracting(TravelDaySummaryDTO::getDayOfTravel)
                .as("여행의 방문 정보(방문 장소, 이미지, 순서)를 일자가 작은 순서(ASC)로 그룹화해 반환해야 한다.")
                .containsExactly(1, 2);
    }

    @Test
    public void 방문_순서_정렬() throws Exception {
        // when
        List<TravelDaySummaryDTO> result = travelFindVisitsService.find(1L);

        // then
        assertThat(result)
                .hasSize(2)
                .as("방문 정보는 방문 순서(order_of_visit)가 작은 순서(ASC)로 반환해야 한다.")
                .satisfiesExactly(
                        daySummary -> assertThat(daySummary.getVisits())
                                .extracting(VisitDTO::getOrderOfVisit)
                                .containsExactly(2, 3),
                        daySummary -> assertThat(daySummary.getVisits())
                                .extracting(VisitDTO::getOrderOfVisit)
                                .containsExactly(1, 2)
                );
    }


    @Test
    void 방문지_개별_클러스터링() throws Exception {
        // when
        List<TravelDaySummaryDTO> result = travelFindVisitsService.find(1L);

        // then
        assertThat(result)
                .hasSize(2)
                .satisfiesExactly(
                        daySummary -> assertThat(daySummary.getBound().getVisits())
                                .as("일정 거리 이상 떨어진 경우 클러스터 영역에 포함하지 않는다.")
                                .isEqualTo(List.of(
                                        createBound(35.99, 135.99, 36.01, 136.01),
                                        createBound(31.99, 124.99, 32.01, 125.01))),

                        daySummary -> assertThat(daySummary.getBound().getVisits())
                                .as("일정 거리 안에 있는 관광지를 포함한 영역을 반환한다.")
                                .isEqualTo(List.of(
                                        createBound(32.99, 125.99, 33.019999999999996, 126.02000000000001),
                                        createBound(32.99, 125.99, 33.019999999999996, 126.02000000000001)))
                );
    }

    @Test
    void 방문지_전체_클러스터링() throws Exception {
        // when
        List<TravelDaySummaryDTO> result = travelFindVisitsService.find(1L);

        // then
        assertThat(result)
                .hasSize(2)
                .satisfiesExactly(
                        daySummary -> assertThat(daySummary.getBound().getEntire())
                                .as("첫 번째 여행 일자의 전체 영역 검증")
                                .isEqualTo(createBound(32.0, 125.0, 36.0, 136.0)),
                        daySummary -> assertThat(daySummary.getBound().getEntire())
                                .as("두 번째 여행 일자의 전체 영역 검증")
                                .isEqualTo(createBound(33.0, 126.0, 33.01, 126.01))
                );
    }


    private Visit createVisit(Long id, int orderOfVisit, int dayOfTravel, String placeName, PlaceType placeType, double lat, double lon) {
        Place place = Place.builder()
                .name(placeName)
                .type(placeType)
                .location(new Location(lat, lon))
                .build();

        return Visit.builder()
                .id(id)
                .place(place)
                .orderOfVisit(orderOfVisit)
                .dayOfTravel(dayOfTravel)
                .travel(travel)
                .build();
    }

    private BoundDTO createBound(double lat1, double lon1, double lat2, double lon2) {
        return new BoundDTO(new LocationDTO(lat1, lon1), new LocationDTO(lat2, lon2));
    }
}