package com.yeohangttukttak.api.domain.travel.dao;

import com.yeohangttukttak.api.domain.travel.entity.TravelReport;
import com.yeohangttukttak.api.global.interfaces.BaseRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TravelReportRepository implements BaseRepository<TravelReport, Long> {

    private final EntityManager em;

    @Override
    public Long save(TravelReport entity) {
        em.persist(entity);
        return entity.getId();
    }

    @Override
    public Optional<TravelReport> find(Long id) {
        TravelReport travelReport = em.find(TravelReport.class, id);
        return Optional.ofNullable(travelReport);
    }

    @Override
    public void delete(TravelReport entity) {
        em.remove(entity);
    }
}
