package com.yeohangttukttak.api.domain.member.entity;

import java.util.Random;

public class NicknameGenerator {

    private static final String[] adjectives = {
        "가냘픈", "가는", "거센", "거친", "고운", "귀여운", "기다란", "날랜", "너그러운", "멋진", "무서운", "밝은", "빠른", "뽀얀", "센",
        "수줍은", "슬픈", "쏜살같은", "아름다운", "약빠른", "어린", "예쁜", "올바른", "잘빠진", "재미있는", "즐거운", "큰", "희망찬", "힘찬"
    };

    private static final Random random = new Random();

    public static String random() {
        int index = random.nextInt(adjectives.length);
        int randomNumber = random.nextInt(10000);

        return String.format("%s 여행자 %04d", adjectives[index], randomNumber);
    }

}
