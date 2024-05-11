package com.yeohangttukttak.api.domain.place.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "place_review")
public class PlaceReview {

    @Id
    private String id;

    @Indexed
    private Long placeId;

    private Integer rating;

    private String comment;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    public PlaceReview(Long placeId, Integer rating, String comment) {
        this.placeId = placeId;
        this.rating = rating;
        this.comment = comment;
    }
}
