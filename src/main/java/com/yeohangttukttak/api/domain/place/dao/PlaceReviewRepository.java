package com.yeohangttukttak.api.domain.place.dao;

import com.yeohangttukttak.api.domain.place.entity.PlaceReview;
import com.yeohangttukttak.api.global.interfaces.BaseRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlaceReviewRepository implements BaseRepository<PlaceReview, Long> {

    private final EntityManager em;

    public List<PlaceReview> findByPlace(Long placeId) {
        return em.createQuery(
                "SELECT pr FROM PlaceReview as pr " +
                        "WHERE pr.place.id = :placeId", PlaceReview.class)
                .setParameter("placeId", placeId)
                .getResultList();
    }

    @Override
    public Long save(PlaceReview entity) {
       em.persist(entity);
       return entity.getId();
    }

    @Override
    public Optional<PlaceReview> find(Long id) {
        return Optional.ofNullable(em.find(PlaceReview.class, id));
    }

    @Override
    public void delete(PlaceReview entity) {
        em.remove(entity);
    }
}
