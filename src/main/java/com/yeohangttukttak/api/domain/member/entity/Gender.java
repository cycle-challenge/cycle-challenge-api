package com.yeohangttukttak.api.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yeohangttukttak.api.global.interfaces.ValueBasedEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Gender implements ValueBasedEnum {

    MALE("male"),
    FEMALE("female");

    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    public static Gender fromString(String value) {

        String lower = value.toLowerCase();

        for (Gender gender : Gender.values()) {

            if (gender.value.equals(lower)) return gender;
        }

        return null;

    }

}
