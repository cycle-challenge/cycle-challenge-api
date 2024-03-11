package com.yeohangttukttak.api.domain.travel.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yeohangttukttak.api.global.interfaces.ValueBasedEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransportType implements ValueBasedEnum {
    CAR ("car"),
    PUBLIC ("public"),
    CYCLE("cycle"),
    WORK("work"),
    OTHERS("others");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
