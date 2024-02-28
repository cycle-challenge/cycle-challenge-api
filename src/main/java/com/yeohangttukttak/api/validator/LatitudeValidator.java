package com.yeohangttukttak.api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

class LatitudeValidator implements ConstraintValidator<ValidLatitude, Double> {

    @Override
    public boolean isValid(Double latitude, ConstraintValidatorContext context) {
        return latitude != null && latitude >= -90 && latitude <= 90;
    }
}
