package com.yeohangttukttak.api.domain.file;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Image extends File {

    @Id @GeneratedValue
    private Long id;

    private int width;

    private int height;

}
