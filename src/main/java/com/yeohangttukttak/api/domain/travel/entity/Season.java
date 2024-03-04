package com.yeohangttukttak.api.domain.travel.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yeohangttukttak.api.global.interfaces.ValueBasedEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Season implements ValueBasedEnum {

    SPRING("spring"),
    SUMMER("summer"),
    AUTUMN("autumn"),
    WINTER("winter");

    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    public static Season valueOf(int month) {

        switch (month) {
            case 12, 1, 2 -> { return Season.WINTER; }
            case 3, 4, 5 -> { return Season.SPRING; }
            case 6, 7, 8 -> { return Season.SUMMER; }
            case 9, 10, 11 -> { return Season.AUTUMN; }
        }

        throw new IllegalArgumentException("month는 1과 12 사이의 정수여야 합니다.");
    }

}
