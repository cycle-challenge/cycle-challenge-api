package com.yeohangttukttak.api.domain.place;

import com.yeohangttukttak.api.domain.Attachable;
import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.file.File;
import com.yeohangttukttak.api.domain.travel.*;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Place extends BaseEntity implements Attachable {

    @Id @GeneratedValue
    @Column(name = "place_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PlaceType type;

    private Point point;

    @OneToMany(mappedBy = "place")
    private List<Visit> visits = new ArrayList<>();

    @OneToMany(mappedBy = "source")
    @SQLRestriction("source_type = 'Place'")
    private List<File> files = new ArrayList<>();

    @Builder
    public Place(Long id, String name, PlaceType type, Location location) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.point = location.getPoint();
    }

}
