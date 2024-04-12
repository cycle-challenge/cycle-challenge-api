package com.yeohangttukttak.api.global.config.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class NicknameConstraintValidatorTest {


    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 성공_케이스() {
        // given
        String nickname = "유저123";

        // when
        Set<ConstraintViolation<TestClass>> violations = validator.validateValue(TestClass.class, "nickname", nickname);

        // then
        assertThat(violations)
                .as("Nickname should be valid")
                .isEmpty();
    }

    @Test
    void 닉네임_길이_짧은_케이스() {
        // given
        String nickname = "A2";

        // when
        Set<ConstraintViolation<TestClass>> violation = validator.validateValue(
                TestClass.class, "nickname", nickname);

        // then
        assertThat(violation)
                .as("Nickname should be too short")
                .isNotEmpty();
    }

    @Test
    void 닉네임_길이_긴_케이스() {
        // given
        String nickname = "12345678901234567890";

        // when
        Set<ConstraintViolation<TestClass>> violations = validator.validateValue(TestClass.class, "nickname", nickname);

        // then
        assertThat(violations)
                .as("Nickname should be too long")
                .isNotEmpty();
    }

    @Test
    void 닉네임_한글_및_영어_포함_케이스() {
        // given
        String nickname = "_1234567";

        // when
        Set<ConstraintViolation<TestClass>> violations = validator.validateValue(TestClass.class, "nickname", nickname);

        // then
        assertThat(violations)
                .as("Nickname should not contain required character types")
                .isNotEmpty();
    }

    @Test
    void 닉네임_공백_포함_케이스() {
        // given
        String nickname = "A 2";

        // when
        Set<ConstraintViolation<TestClass>> violations = validator.validateValue(TestClass.class, "nickname", nickname);

        // then
        assertThat(violations)
                .as("Nickname should not contain spaces")
                .isNotEmpty();
    }

    @Test
    void 닉네임_특수문자_케이스() {
        // given
        String nickname = "한글*영어";

        // when
        Set<ConstraintViolation<TestClass>> violations = validator.validateValue(TestClass.class, "nickname", nickname);

        // then
        assertThat(violations)
                .as("Nickname should not contain invalid special characters")
                .isNotEmpty();
    }

    // 닉네임 검증을 위한 테스트용 클래스 정의
    private static class TestClass {
        @ValidNickname
        private String nickname;
    }

}