package com.yeohangttukttak.api.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yeohangttukttak.api.global.interfaces.ValueBasedEnum;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@AllArgsConstructor
public enum AgeGroup implements ValueBasedEnum {

    S20("s20"), // 20 ~ 30대
    S30("s30"), // 30 ~ 40대
    S40("s40"), // 40 ~ 50대
    S50("s50"), // 50 ~ 60대
    P60("p60"); // 60대 ~

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    public static AgeGroup parseFromBirthDate(LocalDate birthDate) {

        int age = Period.between(birthDate, LocalDate.now()).getYears();

        if (age < 30) return AgeGroup.S20;

        if (age < 40) return AgeGroup.S30;

        if (age < 50) return AgeGroup.S40;

        if (age < 60) return AgeGroup.S50;

        return AgeGroup.P60;

    }

}
