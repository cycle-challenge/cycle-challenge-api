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

    private String sourceType;

    private Long sourceId;

    @Builder
    public File(Long id, String name, String url, String mimeType) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.mimeType = mimeType;
    }

    public void attach(Attachable source) {
        if (this.sourceId != null || sourceType != null) {
            throw new IllegalStateException("이미 첨부 대상이 존재합니다.");
        }

        if (source.getId() == null) {
            throw new IllegalArgumentException("첨부 대상의 식별자(ID)가 존재하지 않습니다.");
        }

        this.sourceType = source.getClass().getSimpleName();
        this.sourceId = source.getId();

        source.getFiles().add(this);
    }

}
