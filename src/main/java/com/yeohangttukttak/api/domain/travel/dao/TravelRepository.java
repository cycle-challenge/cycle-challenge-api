package com.yeohangttukttak.api.domain.travel.dao;

import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.travel.entity.Visibility;
import com.yeohangttukttak.api.global.interfaces.BaseRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TravelRepository implements BaseRepository<Travel, Long> {

    private final EntityManager em;

    @Override
    public Long save(Travel entity) {
        em.persist(entity);
        return entity.getId();
    }

    @Override
    public Optional<Travel> find(Long id) {
        return Optional.ofNullable(em.find(Travel.class, id));
    }

    @Override
    public void delete(Travel entity) {
        em.remove(entity);
    }

    public List<Travel> findAllByMember(Long memberId) {
        return em.createQuery(
          "SELECT t FROM Travel as t " +
                  "WHERE t.member.id = :memberId", Travel.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<Travel> findAllByPlace(Long placeId) {
        return em.createQuery(
            "SELECT DISTINCT t FROM Travel as t " +
                    "JOIN t.visits as v " +
                    "WHERE t.visibility = :visibility " +
                    "AND v.place.id = :placeId", Travel.class)
                .setParameter("visibility", Visibility.PUBLIC)
                .setParameter("placeId", placeId)
                .getResultList();

    }

}
