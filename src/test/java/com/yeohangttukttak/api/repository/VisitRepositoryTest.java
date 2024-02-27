package com.yeohangttukttak.api.repository;

import com.yeohangttukttak.api.domain.place.Location;
import com.yeohangttukttak.api.domain.place.Place;
import com.yeohangttukttak.api.domain.travel.Travel;
import com.yeohangttukttak.api.domain.travel.Visit;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import(VisitRepository.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VisitRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private VisitRepository visitRepository;

    @Test
    public void findByLocation() throws Exception {
        // given
        Travel travel = Travel.builder().build();

        Location locationA = new Location(36.6665, 127.4945);

        Place placeA = Place.builder()
                        .name("그랜드 플라자 청주 호텔")
                        .location(locationA)
                        .build();

        Visit visitA = Visit.builder().place(placeA).travel(travel).build();

        Location locationB = new Location(37.422, -122.084);

        Place placeB = Place.builder()
                        .name("Google 본사")
                        .location(locationB)
                        .build();

        Visit visitB = Visit.builder().place(placeB).travel(travel).build();

        entityManager.persist(visitA);
        entityManager.persist(visitB);

        // when
        List<Visit> foundVisits = visitRepository.findByLocation(new Location(36.6600, 127.4900), 3000);

        // then
        assertTrue(foundVisits.contains(visitA), "그랜드 플라자가 있어야 한다.");
        assertFalse(foundVisits.contains(visitB), "구글 본사는 없어야 한다.");
    }
}