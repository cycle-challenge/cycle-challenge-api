package com.yeohangttukttak.api.domain.visit.dao;

import com.yeohangttukttak.api.domain.visit.entity.Visit;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VisitRepository {

    private final EntityManager em;

    public List<Visit> findByTravel(Long travelID) {
        return em.createQuery(
                        "SELECT v FROM Visit as v " +
                        "WHERE v.travel.id = :travelID", Visit.class)
                .setParameter("travelID", travelID).getResultList();
    }

}
