package com.yeohangttukttak.api.repository;

import com.yeohangttukttak.api.domain.place.Location;
import com.yeohangttukttak.api.domain.place.Place;
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
class VisitRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private VisitRepository visitRepository;

    @Test
    public void search() throws Exception {
        // given
        Location locationA = new Location(36.6665, 127.4945);

        Place placeA = Place.builder()
                        .name("그랜드 플라자 청주 호텔")
                        .location(locationA)
                        .build();

        Location locationB = new Location(37.422, -122.084);

        Place placeB = Place.builder()
                        .name("Google 본사")
                        .location(locationB)
                        .build();

        entityManager.persist(placeA);
        entityManager.persist(placeB);

        // when
        List<Place> foundPlaces = visitRepository.findByLocation(locationA, 3000);


        // then
        assertTrue(foundPlaces.contains(placeA), "그랜드 플라자가 있어야 한다.");
        assertFalse(foundPlaces.contains(placeB), "구글 본사는 없어야 한다.");
    }
}