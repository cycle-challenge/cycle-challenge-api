package com.yeohangttukttak.api.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yeohangttukttak.api.global.interfaces.ValueBasedEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AuthType implements ValueBasedEnum {

    LOCAL("Local"),
    GOOGLE("Google"),
    APPLE("Apple"),
    NONE("None");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

}
