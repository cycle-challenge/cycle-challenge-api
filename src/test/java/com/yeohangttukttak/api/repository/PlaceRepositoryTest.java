package com.yeohangttukttak.api.repository;

import com.yeohangttukttak.api.domain.place.dao.PlaceRepository;
import com.yeohangttukttak.api.domain.place.dto.FindPlaceNearbyQueryDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(PlaceRepository.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlaceRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    public void 범위_검색() {
        // given
        Place placeA = Place.builder()
                .location(new Location(36.6665, 127.4945))
                .build();

        Place placeB = Place.builder()
                .location(new Location(36.6347, 127.4784))
                .build();

        em.persist(placeA);
        em.persist(placeB);

        Location location = new Location(36.6600, 127.4900);
        int radius = 2000;

        // when
        List<FindPlaceNearbyQueryDTO> results = placeRepository.findNearby(location, radius);

        results.forEach(result -> System.out.println(result.getDistance()));

        // then
        assertThat(results)
                .extracting(FindPlaceNearbyQueryDTO::getPlace)
                .as("2KM 이내의 장소는 반환되어야 한다.")
                .contains(placeA)
                .as("2KM 이상 떨어진 장소는 반한되서는 안된다.")
                .doesNotContain(placeB);
    }

    @Test
    public void 거리_반환() {
        // given
        Place placeA = Place.builder()
                .location(new Location(36.6665, 127.4945))
                .build();

        Place placeB = Place.builder()
                .location(new Location(36.6347, 127.4784))
                .build();

        em.persist(placeA);
        em.persist(placeB);

        Location location = new Location(36.6600, 127.4900);
        int radius = 3000;

        // when
        List<FindPlaceNearbyQueryDTO> results = placeRepository.findNearby(location, radius);


        // then
        assertThat(results)
                .extracting(FindPlaceNearbyQueryDTO::getDistance)
                .extracting(Math::floor)
                .as("두 장소 모두 반환해야 한다.")
                .hasSize(2)
                .as("중점에서 미터 단위로 떨어진 거리를 반환해야 한다.")
                .containsExactly(826.0, 2997.0);

    }
}