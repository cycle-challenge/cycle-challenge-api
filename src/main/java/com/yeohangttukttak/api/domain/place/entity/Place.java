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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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

    @OneToMany(mappedBy = "place", cascade = CascadeType.PERSIST)
    private List<Image> images = new ArrayList<>();

    @OneToOne
    @JoinColumn(name ="preview_image_1_id")
    private Image previewImage1;

    @OneToOne
    @JoinColumn(name ="preview_image_2_id")
    private Image previewImage2;

    @OneToOne
    @JoinColumn(name ="preview_image_3_id")
    private Image previewImage3;

    @OneToOne
    @JoinColumn(name ="preview_image_4_id")
    private Image previewImage4;

    @OneToOne
    @JoinColumn(name ="preview_image_5_id")
    private Image previewImage5;

    @Builder
    public Place(Long id, String name, PlaceType type, Location location) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
    }

    public List<Image> getPreviewImages() {

        return Stream.of(previewImage1, previewImage2, previewImage3, previewImage4, previewImage5)
                .filter(Objects::nonNull)
                .toList();

    }

}
