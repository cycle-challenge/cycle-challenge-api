package com.yeohangttukttak.api.domain.visit.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeohangttukttak.api.domain.member.entity.AgeGroup;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.travel.entity.AccompanyType;
import com.yeohangttukttak.api.domain.travel.entity.Motivation;
import com.yeohangttukttak.api.domain.travel.entity.TransportType;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearch;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yeohangttukttak.api.domain.place.entity.QPlace.place;
import static com.yeohangttukttak.api.domain.travel.entity.QTravel.travel;
import static com.yeohangttukttak.api.domain.visit.entity.QVisit.visit;


@Repository
public class VisitRepository {

    private final JPAQueryFactory queryFactory;

    public VisitRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<Visit> search(VisitSearch search) {
        return queryFactory
                .selectFrom(visit)
                .join(visit.travel, travel).fetchJoin()
                .join(visit.place, place).fetchJoin()
                .where(dwithin(search.getLocation(), search.getRadius()),
                        motivationEq(search.getMotivation()),
                        accompanyTypeEq(search.getAccompanyType()),
                        ageGroupEq(search.getAgeGroup()),
                        transportTypeEq(search.getTransportType())).fetch();
    }

    private BooleanTemplate dwithin(Location location, int radius) {
        return Expressions.booleanTemplate(
                "st_dwithin({0}, {1}, {2}, false) is true",
                place.point,
                location.getPoint(),
                radius
        );
    }

    private BooleanExpression motivationEq(Motivation motivation) {
        return motivation != null ? travel.motivation.eq(motivation) : null;
    }

    private BooleanExpression accompanyTypeEq(AccompanyType accompanyType) {
        return accompanyType != null ? travel.accompanyType.eq(accompanyType) : null;
    }

    private BooleanExpression ageGroupEq(AgeGroup ageGroup) {
        return ageGroup != null ? travel.member.ageGroup.eq(ageGroup) : null;
    }

    private BooleanExpression transportTypeEq(TransportType transportType) {
        return transportType != null ? travel.transportType.eq(transportType) : null;
    }

}
