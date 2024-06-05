package com.yeohangttukttak.api.domain.place.dao;

import com.yeohangttukttak.api.domain.place.entity.PlaceReviewReport;
import com.yeohangttukttak.api.global.interfaces.BaseRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlaceReviewReportRepository implements BaseRepository<PlaceReviewReport, Long> {

    private final EntityManager em;

    @Override
    public Long save(PlaceReviewReport entity) {
        em.persist(entity);
        return entity.getId();
    }

    @Override
    public Optional<PlaceReviewReport> find(Long id) {
        PlaceReviewReport report = em.find(PlaceReviewReport.class, id);

        return Optional.ofNullable(report);
    }

    @Override
    public void delete(PlaceReviewReport entity) {
        em.remove(entity);
    }
}
