package com.yeohangttukttak.api.domain.place.dao;

import com.yeohangttukttak.api.domain.place.dto.PlaceReviewReportDto;
import com.yeohangttukttak.api.domain.place.entity.PlaceReview;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class PlaceReviewRepository {

    private final MongoTemplate mongoTemplate;

    public String insert(PlaceReview review) {
        PlaceReview insertedReview = mongoTemplate.insert(review);

        return insertedReview.getId();
    }

    public List<PlaceReview> findByPlace(Long placeId) {

        return mongoTemplate.find(query(where("placeId").is(placeId)),
                PlaceReview.class, "place_review");

    }

    public List<PlaceReviewReportDto> report(List<Long> placeIds) {

        MatchOperation matchOperation = Aggregation.match(where("placeId").in(placeIds));

        GroupOperation groupOperation = Aggregation.group("placeId")
                .count().as("count")
                .avg("rating").as("ratingAvg");

        return mongoTemplate.aggregate(newAggregation(matchOperation, groupOperation), "place_review", Document.class)
                .getMappedResults()
                .stream().map(PlaceReviewReportDto::new)
                .toList();

    }




}
