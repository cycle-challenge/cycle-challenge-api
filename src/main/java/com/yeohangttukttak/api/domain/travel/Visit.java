package com.yeohangttukttak.api.domain.travel;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.place.Place;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Visit extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private int dayOfTravel;

    private int orderOfVisit;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @ManyToOne
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
