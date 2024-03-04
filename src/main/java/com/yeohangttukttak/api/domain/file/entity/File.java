package com.yeohangttukttak.api.domain.file.entity;

import com.yeohangttukttak.api.global.interfaces.Attachable;
import com.yeohangttukttak.api.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class File extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String url;

    private String mimeType;

    @Builder
    public File(Long id, String name, String url, String mimeType) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.mimeType = mimeType;
    }

}
