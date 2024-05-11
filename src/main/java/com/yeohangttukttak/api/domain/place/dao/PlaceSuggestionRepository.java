package com.yeohangttukttak.api.domain.place.dao;
import com.yeohangttukttak.api.domain.place.entity.PlaceSuggestion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlaceSuggestionRepository {

    private final MongoTemplate template;

    public List<PlaceSuggestion> search(String query) {

        AggregationOperation autocompleteOperation = Aggregation.stage(String.format("""
                { $search: {
                    "index": "place_search_index",
                    "compound": {
                        "should": [
                            { "autocomplete": {
                                "query": "%s",
                                "path": "name"
                            }},
                            { "autocomplete": {
                                "query": "%s",
                                "path": "localAddr"
                            }},
                            { "autocomplete": {
                                "query": "%s",
                                "path": "roadAddr"
                            }}
                        ]
                    }
                }}
                """, query, query, query));

        LimitOperation limitOperation = Aggregation.limit(10L);

        return template.aggregate(Aggregation.newAggregation(autocompleteOperation, limitOperation),
                        "place_suggestion", PlaceSuggestion.class)
                .getMappedResults();
    }

}