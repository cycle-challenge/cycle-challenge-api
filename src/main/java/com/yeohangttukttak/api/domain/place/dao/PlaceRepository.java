package com.yeohangttukttak.api.domain.place.dao;

import com.yeohangttukttak.api.domain.place.dto.FindPlaceNearbyQueryDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlaceRepository {

    private final EntityManager em;

    public List<FindPlaceNearbyQueryDTO> findNearby(Location location, int radius) {
        return em.createQuery("SELECT new com.yeohangttukttak.api.domain.place.dto" +
                        ".FindPlaceNearbyQueryDTO(p, st_distance(p.location.point, :point)) " +
                        "FROM Place as p " +
                        "WHERE dwithin(p.location.point, :point, :radius, false)", FindPlaceNearbyQueryDTO.class)
                .setParameter("point", location.getPoint())
                .setParameter("radius", radius)
                .getResultList();
    }

}
