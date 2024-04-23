package com.yeohangttukttak.api.global.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiControllerAdvice {

    private final MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<List<ApiError>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, Locale locale) {

        List<ApiError> errors = ex.getFieldErrors()
                .stream().map(fieldError ->
                        new ApiError(
                                messageSource.getMessage(fieldError, locale),
                                fieldError.getField()
                        ))
                .toList();

        return new ApiResponse<>(ApiErrorCode.INVALID_ARGUMENT, errors);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleApiException(ApiException ex, Locale locale) {

        ApiErrorCode errorCode = ex.getErrorCode();
        String message = messageSource.getMessage(errorCode.name(), null, locale);

        ApiError error = new ApiError(message, errorCode.getTarget());

        return new ResponseEntity<>(new ApiResponse<>(errorCode, error), errorCode.getStatus());

    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<ApiError>> handleUnhandledException(Exception ex, Locale locale) {

        ApiErrorCode errorCode = ApiErrorCode.INTERNAL_SERVER_ERROR;
        String message = messageSource.getMessage(errorCode.name(), null, locale);

        ApiError error = new ApiError(message, null);

        return new ResponseEntity<>(new ApiResponse<>(errorCode, error), errorCode.getStatus());

    }

}
