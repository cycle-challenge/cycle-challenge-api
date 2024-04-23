package com.yeohangttukttak.api.domain.place.entity;

import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.file.entity.Image;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.global.interfaces.Bookmarkable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Place extends BaseEntity implements Bookmarkable {

    @Id @GeneratedValue
    @Column(name = "place_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PlaceType type;

    @Embedded
    private Location location;

    private String googlePlaceId;

    @OneToMany(mappedBy = "place")
    private List<Visit> visits = new ArrayList<>();

    @OneToMany(mappedBy = "place")
    private List<Image> images = new ArrayList<>();

    @Builder
    public Place(Long id, String name, PlaceType type, Location location) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
    }

}
