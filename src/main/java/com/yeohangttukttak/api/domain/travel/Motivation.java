package com.yeohangttukttak.api.domain.travel;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Motivation {
    EXIST("existing"),          // 재미
    EDUCATION("education"),     // 교육
    SOCIAL("social"),           // 친목
    REFLECT("reflect"),         // 성찰
    EXPERIENCE("experience"),   // 경험
    RELAX("relax");             // 힐링

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
