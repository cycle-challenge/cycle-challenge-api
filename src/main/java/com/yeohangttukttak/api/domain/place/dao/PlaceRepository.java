package com.yeohangttukttak.api.domain.place.dao;

import com.yeohangttukttak.api.domain.place.dto.PlaceFindNearbyQueryDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import jakarta.persistence.EntityManager;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.global.common.PageResult;
import com.yeohangttukttak.api.global.common.PageSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlaceRepository {

    private final EntityManager em;

    public List<PlaceFindNearbyQueryDTO> findNearby(Location location, int radius) {
        return em.createQuery("SELECT new com.yeohangttukttak.api.domain.place.dto" +
                        ".PlaceFindNearbyQueryDTO(p, distance_sphere(p.location.point, :point)) " +
                        "FROM Place as p " +
                        "WHERE dwithin(p.location.point, :point, :radius, false)", PlaceFindNearbyQueryDTO.class)
                .setParameter("point", location.getPoint())
                .setParameter("radius", radius)
                .getResultList();
    }

    public PageResult<Image> getImage(Long id, PageSearch search) {
        return new PageResult<>(em.createQuery(
                "SELECT i FROM Image as i " +
                        "JOIN i.place as p " +
                        "WHERE p.id = :id " +
                        "ORDER BY p.id", Image.class)
                .setParameter("id", id)
                .setFirstResult(search.getOffset())
                .setMaxResults(search.getPageSize() + 1)
                .getResultList(), search);
    }
}
