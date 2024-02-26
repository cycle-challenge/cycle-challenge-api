package com.yeohangttukttak.api.repository;

import com.yeohangttukttak.api.domain.place.Location;
import com.yeohangttukttak.api.domain.travel.Visit;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VisitRepository {

    private final EntityManager entityManager;
    
    public List<Visit> findByLocation(Location location, int radius) {
        return entityManager.createQuery(
                        "SELECT v FROM Visit as v" +
                                " JOIN FETCH v.place as p" +
                                " JOIN FETCH v.travel as t" +
                                " WHERE st_dwithin(p.point, :point, :radius, false) is true", Visit.class)
                .setParameter("point", location.getPoint())
                .setParameter("radius", radius)
                .getResultList();
    }
}
