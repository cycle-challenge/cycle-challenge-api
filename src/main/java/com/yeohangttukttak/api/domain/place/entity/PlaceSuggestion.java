package com.yeohangttukttak.api.domain.place.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "place_suggestion")
public class PlaceSuggestion {

    private String name;

    private String localAddr;

    private String roadAddr;

    private Long placeId;

}
