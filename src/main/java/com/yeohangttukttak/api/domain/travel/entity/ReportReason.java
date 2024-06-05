package com.yeohangttukttak.api.domain.travel.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yeohangttukttak.api.global.interfaces.ValueBasedEnum;

public enum ReportReason implements ValueBasedEnum {

    PORNO("porno"), VIOLENT("violent"), SPAM("spam"),
    PRIVACY("privacy"), OTHER("other"), COPYRIGHT("copyright");

    private final String value;

    ReportReason(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return value;
    }
}
