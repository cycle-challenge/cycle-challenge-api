package com.yeohangttukttak.api.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeohangttukttak.api.domain.place.Location;
import com.yeohangttukttak.api.domain.travel.Motivation;
import com.yeohangttukttak.api.domain.travel.Visit;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yeohangttukttak.api.domain.place.QPlace.place;
import static com.yeohangttukttak.api.domain.travel.QTravel.travel;
import static com.yeohangttukttak.api.domain.travel.QVisit.visit;

@Repository
public class VisitRepository {

    private final JPAQueryFactory queryFactory;

    public VisitRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<Visit> search(Location location, int radius) {
        return queryFactory
                .selectFrom(visit)
                .join(visit.travel, travel).fetchJoin()
                .join(visit.place, place).fetchJoin()
                .where(dwithin(location, radius)).fetch();
    }

    private BooleanTemplate dwithin(Location location, int radius) {
        return Expressions.booleanTemplate(
                "st_dwithin({0}, {1}, {2}, false) is true",
                place.point,
                location.getPoint(),
                radius
        );
    }
}
