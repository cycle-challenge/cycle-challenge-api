package com.yeohangttukttak.api.domain.place.dao;

import com.yeohangttukttak.api.domain.place.dto.PlaceFindNearbyQueryDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.global.interfaces.BaseRepository;
import jakarta.persistence.EntityManager;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.global.common.PageResult;
import com.yeohangttukttak.api.global.common.PageSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class PlaceRepository implements BaseRepository<Place, Long> {

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

    public List<Place> findAllById(Set<Long> ids) {
        return em.createQuery(
                "SELECT p FROM Place as p " +
                        "WHERE p.id IN :ids", Place.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public Long save(Place entity) {
        em.persist(entity);
        return entity.getId();
    }

    @Override
    public Optional<Place> find(Long id) {
        return Optional.ofNullable(em.find(Place.class, id));
    }

    @Override
    public void delete(Place entity) {
        em.remove(entity);
    }

    public Optional<Place> findByGooglePlaceId(String googlePlaceId) {

        List<Place> places = em.createQuery(
                "SELECT p FROM Place as p " +
                "WHERE p.googlePlaceId = :googlePlaceId", Place.class)
                .setParameter("googlePlaceId", googlePlaceId)
                .getResultList();

        return places.stream().findFirst();
    }
}
