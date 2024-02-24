package com.yeohangttukttak.api.domain.place;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.travel.*;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Place extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "place_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PlaceType type;

    private Point point;

    @OneToMany(mappedBy = "place")
    private List<Visit> visits = new ArrayList<>();


    @Builder
    public Place(String name, PlaceType type, Location location) {
        this.name = name;
        this.type = type;
        this.point = location.getPoint();
    }

}
