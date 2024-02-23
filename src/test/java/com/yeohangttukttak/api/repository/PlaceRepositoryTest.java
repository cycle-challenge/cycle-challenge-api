package com.yeohangttukttak.api.repository;

import com.yeohangttukttak.api.domain.place.Place;
import com.yeohangttukttak.api.repository.PlaceRepository.PlaceSearchQuery;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class PlaceRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    public void search() throws Exception {
        // given
        Place placeA = Place.builder()
                        .name("그랜드 플라자 청주 호텔")
                        .latitude(36.6665)
                        .longitude(127.4945)
                        .build();

        Place placeB = Place.builder()
                        .name("Google 본사")
                        .latitude(37.422)
                        .longitude(-122.084)
                        .build();

        entityManager.persist(placeA);
        entityManager.persist(placeB);

        // when
        List<Place> foundPlaces = placeRepository.search(
                new PlaceSearchQuery(
                36.6665, 127.4945, 3000
                ));

        // then
        assertTrue(foundPlaces.contains(placeA), "그랜드 플라자가 있어야 한다.");
        assertFalse(foundPlaces.contains(placeB), "구글 본사는 없어야 한다.");
    }
}