package com.yeohangttukttak.api.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, Locale locale) {

        List<ApiError> errors = ex.getFieldErrors()
                .stream().map(fieldError ->
                        new ApiError(ApiErrorCode.INVALID_ARGUMENT,
                                messageSource.getMessage(fieldError, locale))
                )
                .toList();

        return new ApiErrorResponse(errors);
    }


}
