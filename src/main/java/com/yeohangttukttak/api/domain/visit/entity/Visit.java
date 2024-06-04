package com.yeohangttukttak.api.domain.visit.entity;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.visit.dto.VisitCreateDto;
import com.yeohangttukttak.api.domain.visit.dto.VisitModifyDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Visit extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "visit_id")
    private Long id;

    private Integer dayOfTravel;

    private Integer orderOfVisit;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "travel_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Travel travel;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "place_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Place place;

    @OneToMany(mappedBy = "visit")
    private List<Image> images = new ArrayList<>();

    @Builder
    public Visit(Long id, Integer dayOfTravel, Integer orderOfVisit, Place place, Travel travel) {
        this.id = id;
        this.dayOfTravel = dayOfTravel;
        this.orderOfVisit = orderOfVisit;
        this.place = place;
        this.travel = travel;
    }

    public void setDayOfTravel(Integer dayOfTravel) {
        this.dayOfTravel = dayOfTravel;
    }

    public void setOrderOfVisit(Integer orderOfVisit) {
        this.orderOfVisit = orderOfVisit;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
