package com.yeohangttukttak.api.domain.travel.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yeohangttukttak.api.global.interfaces.ValueBasedEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Visibility implements ValueBasedEnum {

    PUBLIC("public"),
    PRIVATE("private");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

}
