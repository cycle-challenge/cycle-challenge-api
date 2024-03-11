package com.yeohangttukttak.api.domain.file.entity;

import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.global.interfaces.Attachable;
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
public class File extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "file_id")
    private Long id;

    private String name;

    private String url;

    private String mimeType;

//    @OneToOne(fetch = LAZY)
//    private Travel travel;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "visit_id")
    private Visit visit;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @Builder
    public File(Long id, String name, String url, String mimeType) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.mimeType = mimeType;
    }

}
