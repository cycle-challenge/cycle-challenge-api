package com.yeohangttukttak.api.domain.member.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordTest {

    @Test
    public void 생성_테스트() throws Exception {
        // given
        String plainPassword = "heebeom123";

        // when
        Password passwordA = new Password(plainPassword);
        Password passwordB = new Password(plainPassword);

        // then
        assertThat(passwordA.getPassword())
                .as("평문 비밀번호가 같아도 해시된 값은 달라야 한다")
                .isNotEqualTo(passwordB.getPassword());
    }

    @Test
    public void 검증_테스트() throws Exception {
        // given
        String plainPassword = "heebeom123";
        String wrongPassword = "heejin123";
        Password password = new Password(plainPassword);

        // when
        boolean shouldValid = password.validate(plainPassword);
        boolean shouldNotValid = password.validate(wrongPassword);

        // then
        assertThat(shouldValid)
                .as("같은 평문 비밀번호가 주어졌을 때 검증 값이 참이여야 한다.")
                .isTrue();

        assertThat(shouldNotValid)
                .as("비밀번호가 다를 때는 검증에 실패해야 한다.")
                .isFalse();
    }

}