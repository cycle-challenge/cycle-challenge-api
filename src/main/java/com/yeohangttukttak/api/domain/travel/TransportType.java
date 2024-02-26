package com.yeohangttukttak.api.domain.travel;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransportType {
    CAR ("car"),
    PUBLIC ("public");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
