package com.yeohangttukttak.api.domain.travel.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yeohangttukttak.api.domain.ValueBasedEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransportType implements ValueBasedEnum {
    CAR ("car"),
    PUBLIC ("public");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
