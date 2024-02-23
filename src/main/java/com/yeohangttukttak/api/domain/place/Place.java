package com.yeohangttukttak.api.domain.place;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.travel.Visit;
import jakarta.persistence.*;
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

    private Point location;

    @OneToMany(mappedBy = "place")
    private List<Visit> visits = new ArrayList<>();

}
