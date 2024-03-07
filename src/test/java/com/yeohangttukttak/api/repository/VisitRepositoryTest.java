package com.yeohangttukttak.api.repository;

import com.yeohangttukttak.api.domain.member.entity.AgeGroup;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.travel.entity.*;
import com.yeohangttukttak.api.domain.visit.dao.VisitSearchResult;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearch;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.domain.visit.dao.VisitRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Import(VisitRepository.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VisitRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private VisitRepository visitRepository;

    Member memberA, memberB;
    Travel travelA, travelB;
    Place placeA, placeB;
    Visit visitA, visitB;

    @BeforeEach
    public void init() {

        memberA = Member.builder()
                .ageGroup(AgeGroup.S20)
                .build();

        travelA = Travel.builder()
                .member(memberA)
                .accompanyType(AccompanyType.PARENTS)
                .motivation(Motivation.RELAX)
                .transportType(TransportType.CAR)
                .period(new TravelPeriod(
                        LocalDate.parse("2022-03-19"),
                        LocalDate.parse("2022-03-21")
                ))
                .build();

        placeA = Place.builder()
                .name("그랜드 플라자 청주 호텔")
                .location(new Location(36.6665, 127.4945))
                .build();

        memberB = Member.builder()
                .ageGroup(AgeGroup.S30)
                .build();

        visitA = Visit.builder().place(placeA).travel(travelA).build();

        travelB = Travel.builder()
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

        placeB = Place.builder()
                .name("청주 시립 미술관")
                .location(new Location(36.6347, 127.4784))
                .build();

        visitB = Visit.builder().place(placeB).travel(travelB).build();

        entityManager.persist(memberA);
        entityManager.persist(memberB);
        entityManager.persist(visitA);
        entityManager.persist(visitB);

    }


    @Test
    public void 범위_검색() throws Exception {
        // given
        VisitSearch search = VisitSearch.builder()
                .location(new Location(36.6600, 127.4900))
                .radius(2000)
                .build();

        // when
        List<VisitSearchResult> results = visitRepository.search(search);

        // then
        assertThat(results)
                .extracting(VisitSearchResult::getVisit)
                .as("그랜드 플라자가 있어야 한다.")
                .contains(visitA);

        assertThat(results)
                .extracting(VisitSearchResult::getVisit)
                .as("구글 본사는 없어야 한다.")
                .doesNotContain(visitB);
    }

    @Test
    public void 연령_검색() throws Exception {
        // given
        VisitSearch search = VisitSearch.builder()
                .location(new Location(36.6600, 127.4900))
                .radius(20000)
                .ageGroups(Set.of(AgeGroup.S20, AgeGroup.P50))
                .build();

        // when
        List<VisitSearchResult> results = visitRepository.search(search);

        // then
        assertThat(results)
                .extracting(VisitSearchResult::getVisit)
                .as("20대의 여행은 반환되어야 한다.")
                .contains(visitA);

        assertThat(results)
                .extracting(VisitSearchResult::getVisit)
                .as("30대의 여행은 반한되지 말아야 한다.")
                .doesNotContain(visitB);
    }


    @Test
    public void 계절_검색() throws Exception {
        // given
        VisitSearch search = VisitSearch.builder()
                .location(new Location(36.6600, 127.4900))
                .radius(20000)
                .seasons(Set.of(Season.SPRING, Season.WINTER))
                .build();

        // when
        List<VisitSearchResult> results = visitRepository.search(search);

        // then
        assertThat(results)
                .extracting(VisitSearchResult::getVisit)
                .as("봄 여행은 반환되어야 한다.")
                .contains(visitA);

        assertThat(results)
                .extracting(VisitSearchResult::getVisit)
                .as("여름 여행은 반환되지 말아야 한다.")
                .doesNotContain(visitB);
    }

    @Test
    public void 동반_유형_검색() throws Exception {
        // given
        VisitSearch search = VisitSearch.builder()
                .location(new Location(36.6600, 127.4900))
                .radius(20000)
                .accompanyTypes(Set.of(AccompanyType.PARENTS, AccompanyType.SOLO))
                .build();

        // when
        List<VisitSearchResult> results = visitRepository.search(search);

        // then
        assertThat(results)
                .extracting(VisitSearchResult::getVisit)
                .as("부모 동반 여행은 반환되어야 한다..")
                .contains(visitA);

        assertThat(results)
                .extracting(VisitSearchResult::getVisit)
                .as("자녀 동반 여행은 반환되지 말아야 한다.")
                .doesNotContain(visitB);
    }

    @Test
    public void 여행_동기_검색() throws Exception {
        // given
        VisitSearch search = VisitSearch.builder()
                .location(new Location(36.6600, 127.4900))
                .radius(20000)
                .motivations(Set.of(Motivation.RELAX, Motivation.EXPERIENCE))
                .build();

        // when
        List<VisitSearchResult> results = visitRepository.search(search);

        // then
        assertThat(results)
                .extracting(VisitSearchResult::getVisit)
                .as("힐링 여행은 반환되어야 한다..")
                .contains(visitA);

        assertThat(results)
                .extracting(VisitSearchResult::getVisit)
                .as("교육 여행은 반환되지 말아야 한다.")
                .doesNotContain(visitB);
    }

    @Test
    public void 이동_수단_검색() throws Exception {
        // given
        VisitSearch search = VisitSearch.builder()
                .location(new Location(36.6600, 127.4900))
                .radius(20000)
                .transportTypes(Set.of(TransportType.CAR))
                .build();

        // when
        List<VisitSearchResult> results = visitRepository.search(search);

        // then
        assertThat(results)
                .extracting(VisitSearchResult::getVisit)
                .as("자차 여행은 반환되어야 한다..")
                .contains(visitA);

        assertThat(results)
                .extracting(VisitSearchResult::getVisit)
                .as("대중교통 여행은 반환되지 말아야 한다.")
                .doesNotContain(visitB);
    }
}