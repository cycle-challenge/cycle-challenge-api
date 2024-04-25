package com.yeohangttukttak.api.domain.travel.dao;

import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.global.interfaces.BaseRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
