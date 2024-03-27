package com.yeohangttukttak.api.domain.file.entity;

import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Image extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    private String name;

    private String path;

    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    private String mimeType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "visit_id")
    private Visit visit;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Builder
    public Image(Long id, String name, String path, String mimeType, Place place) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.mimeType = mimeType;
        this.place = place;
    }
}
