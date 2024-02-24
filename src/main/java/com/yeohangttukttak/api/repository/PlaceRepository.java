package com.yeohangttukttak.api.repository;

import com.yeohangttukttak.api.domain.place.Location;
import com.yeohangttukttak.api.domain.place.Place;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlaceRepository {

    private final EntityManager entityManager;

    public List<Place> findByLocation(Location location, int radius) {
        return entityManager.createQuery(
                "SELECT p FROM Place AS p" +
                        " WHERE st_dwithin(p.point, :point, :radius, false) is true", Place.class)
                .setParameter("point", location.getPoint())
                .setParameter("radius", radius)
                .getResultList();
    }

}
