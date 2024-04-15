package com.yeohangttukttak.api.global.config.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NicknameConstraintValidator implements ConstraintValidator<ValidNickname, String> {

    private static final String NICKNAME_PATTERN = "^[가-힣a-zA-Z0-9_-]{3,15}$";
    private static final String MUST_CONTAIN_KR_OR_EN_PATTERN = ".*[가-힣a-zA-Z]+.*";

    @Override
    public boolean isValid(String nickname, ConstraintValidatorContext context) {
        if (nickname == null) {
            return false;
        }

        // 닉네임이 전체 패턴을 만족하는지 체크
        boolean matchesOverallPattern = nickname.matches(NICKNAME_PATTERN);

        // 닉네임이 한글 혹은 영어를 포함하는지 체크
        boolean containsKrOrEn = nickname.matches(MUST_CONTAIN_KR_OR_EN_PATTERN);

        return matchesOverallPattern && containsKrOrEn;
    }

}
