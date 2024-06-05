package com.yeohangttukttak.api.domain.visit.dao;

import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.travel.entity.Visibility;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.global.interfaces.BaseRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VisitRepository implements BaseRepository<Visit, Long> {

    private final EntityManager em;

    public List<Visit> findNearby(Location location, int radius) {
        return em.createQuery("SELECT v FROM Visit as v " +
                        "JOIN FETCH v.place as p " +
                        "JOIN FETCH v.travel as t " +
                        "WHERE t.visibility = :visibility " +
                        "AND dwithin(p.location.point, :point, :radius, false)", Visit.class)
                .setParameter("visibility", Visibility.PUBLIC)
                .setParameter("point", location.getPoint())
                .setParameter("radius", radius)
                .getResultList();
    }

    @Override
    public Long save(Visit entity) {
       em.persist(entity);
       return entity.getId();
    }

    @Transactional
    public void saveAll(List<Visit> entities) {
        entities.forEach(this::save);
    }

    @Override
    public Optional<Visit> find(Long id) {
        return Optional.ofNullable(em.find(Visit.class, id));
    }

    @Override
    public void delete(Visit entity) {
        em.remove(entity);
    }
}
