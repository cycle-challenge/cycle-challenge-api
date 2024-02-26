package com.yeohangttukttak.api.domain.member;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AgeGroup {
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
