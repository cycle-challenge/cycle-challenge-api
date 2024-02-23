package com.yeohangttukttak.api.domain.member;

public enum AgeGroup {
    TWENTIES("20s"),
    THIRTIES("30s"),
    FORTIES("40s"),
    FIFTY_PLUS("50plus");

    private final String group;

    AgeGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }
}
