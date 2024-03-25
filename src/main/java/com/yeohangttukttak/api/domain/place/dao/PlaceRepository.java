package com.yeohangttukttak.api.domain.place.dao;

import com.yeohangttukttak.api.domain.place.dto.FindPlaceNearbyQueryDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import jakarta.persistence.EntityManager;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.global.common.PageResult;
import com.yeohangttukttak.api.global.common.PageSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import static com.yeohangttukttak.api.domain.file.entity.QImage.image;
import static com.yeohangttukttak.api.domain.place.entity.QPlace.place;

import java.util.List;

@Repository
public class PlaceRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public PlaceRepository (EntityManager entityManager) {
        em = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<FindPlaceNearbyQueryDTO> findNearby(Location location, int radius) {
        return em.createQuery("SELECT new com.yeohangttukttak.api.domain.place.dto" +
                        ".FindPlaceNearbyQueryDTO(p, st_distance(p.location.point, :point)) " +
                        "FROM Place as p " +
                        "WHERE dwithin(p.location.point, :point, :radius, false)", FindPlaceNearbyQueryDTO.class)
                .setParameter("point", location.getPoint())
                .setParameter("radius", radius)
                .getResultList();
    }

    public PageResult<Image> getPlaceImage(Long id, PageSearch search) {

        return em.createQuery(
                "SELECT i from Image as i join i.place"
        )

        List<Image> files = queryFactory.select(image)
                .from(image)
                .join(image.place, place)
                .where(place.id.eq(id))
                .offset(search.getOffset())
                .limit(search.getPageSize() + 1)
                .orderBy(image.id.asc())
                .fetch();

        return new PageResult<>(files, search);
    }
}
