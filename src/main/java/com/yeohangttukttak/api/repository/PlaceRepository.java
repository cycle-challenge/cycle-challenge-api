package com.yeohangttukttak.api.repository;

import com.yeohangttukttak.api.domain.place.Place;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlaceRepository {

    private final EntityManager entityManager;

    List<Place> search(PlaceSearchQuery query) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(query.getLongitude(), query.getLatitude()));

        return entityManager.createQuery(
                "SELECT p FROM Place AS p" +
                        " WHERE st_dwithin(p.location, :point, :distance, false) is true", Place.class)
                .setParameter("point", point)
                .setParameter("distance", query.getDistance())
                .getResultList();
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class PlaceSearchQuery {
        private double latitude;
        private double longitude;
        private int distance;
    }
}
