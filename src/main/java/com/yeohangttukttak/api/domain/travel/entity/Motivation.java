package com.yeohangttukttak.api.domain.travel.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yeohangttukttak.api.global.interfaces.ValueBasedEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Motivation implements ValueBasedEnum {
    REFRESH("refresh"),          // 재미
    RELAX("relax"),
    EDU("education"),     // 교육
    SOCIAL("social"),           // 친목
    REFLECT("reflect"),         // 성찰
    SNS("sns"),
    ENERGY("energy"),
    EXPR("experience"),   // 경험
    OTHERS("others");             // 힐링

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
