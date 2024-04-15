package com.yeohangttukttak.api.global.config.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordConstraintValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 비밀번호_길이_미달_케이스() {
        // given
        String password = "Ab1!";

        // when
        Set<ConstraintViolation<TestClass>> violations = validator.validateValue(TestClass.class, "password", password);

        // then
        assertThat(violations)
                .as("Password should meet length requirements")
                .isNotEmpty();
    }

    @Test
    void 비밀번호_적합한_케이스() {
        // given
        String password = "Valid1!Password";

        // when
        Set<ConstraintViolation<TestClass>> violations = validator.validateValue(TestClass.class, "password", password);

        // then
        assertThat(violations)
                .as("Password should be valid")
                .isEmpty();
    }

    @Test
    void 비밀번호_특수문자_누락_케이스() {
        // given
        String password = "Password1WithoutSpecial";

        // when
        Set<ConstraintViolation<TestClass>> violations = validator.validateValue(TestClass.class, "password", password);

        // then
        assertThat(violations)
                .as("Password should include at least one special character")
                .isNotEmpty();
    }

    @Test
    void 비밀번호_대문자_누락_케이스() {
        // given
        String password = "valid1!password";

        // when
        Set<ConstraintViolation<TestClass>> violations = validator.validateValue(TestClass.class, "password", password);

        // then
        assertThat(violations)
                .as("Password should include at least one uppercase letter")
                .isNotEmpty();
    }

    @Test
    void 비밀번호_소문자_누락_케이스() {
        // given
        String password = "VALID1!PASSWORD";

        // when
        Set<ConstraintViolation<TestClass>> violations = validator.validateValue(TestClass.class, "password", password);

        // then
        assertThat(violations)
                .as("Password should include at least one lowercase letter")
                .isNotEmpty();
    }

    // 비밀번호 검증을 위한 테스트용 클래스 정의
    private static class TestClass {
        @ValidPassword
        private String password;
    }
}