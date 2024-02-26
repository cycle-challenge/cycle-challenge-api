package com.yeohangttukttak.api.domain.travel;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.place.Place;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Visit extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private int dayOfTravel;

    private int orderOfVisit;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Builder
    public Visit(int dayOfTravel, int orderOfVisit, Place place, Travel travel) {
        this.dayOfTravel = dayOfTravel;
        this.orderOfVisit = orderOfVisit;
        this.place = place;
        this.travel = travel;

        place.getVisits().add(this);
    }

}
