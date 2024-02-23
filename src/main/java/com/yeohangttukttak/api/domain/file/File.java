package com.yeohangttukttak.api.domain.file;

import com.yeohangttukttak.api.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class File extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String url;

    private String type;

    private int size;

}
