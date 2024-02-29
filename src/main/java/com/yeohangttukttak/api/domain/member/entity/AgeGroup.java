package com.yeohangttukttak.api.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yeohangttukttak.api.global.interfaces.ValueBasedEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AgeGroup implements ValueBasedEnum {
    S20("20s"), // 20 ~ 30대
    S30("30s"), // 30 ~ 40대
    S40("40s"), // 40 ~ 50대
    P50("50p");  // 50대 이상

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

}
