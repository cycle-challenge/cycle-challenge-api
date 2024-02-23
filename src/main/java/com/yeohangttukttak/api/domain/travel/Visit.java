package com.yeohangttukttak.api.domain.travel;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.place.Place;
import jakarta.persistence.*;

@Entity
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

}
