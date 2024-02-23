package com.yeohangttukttak.api.domain.place;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.travel.*;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

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

    private Point location;

    @OneToMany(mappedBy = "place")
    private List<Visit> visits = new ArrayList<>();


    @Builder
    public Place(String name, PlaceType type, Double latitude, Double longitude) {
        this.name = name;
        this.type = type;

        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
        this.location = factory.createPoint(new Coordinate(longitude, latitude));
    }

}
