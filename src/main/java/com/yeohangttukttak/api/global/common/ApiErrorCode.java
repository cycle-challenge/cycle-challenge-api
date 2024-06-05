package com.yeohangttukttak.api.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ApiErrorCode {

    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST),
    INVALID_BLOCK_MEMBER(HttpStatus.BAD_REQUEST),

    DUPLICATED_EMAIL(HttpStatus.CONFLICT),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT),
    DUPLICATED_BOOKMARK(HttpStatus.CONFLICT),
    DUPLICATED_SOCIAL_EMAIL(HttpStatus.CONFLICT),
    MEMBER_ALREADY_BLOCKED(HttpStatus.CONFLICT),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND),
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND),
    PLACE_REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND),
    TRAVEL_NOT_FOUND(HttpStatus.NOT_FOUND),
    VISIT_NOT_FOUND(HttpStatus.NOT_FOUND),
    BOOKMARK_TARGET_NOT_FOUND(HttpStatus.NOT_FOUND),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND),

    SIGN_IN_FAILED(HttpStatus.UNAUTHORIZED),
    AUTHORIZATION_REQUIRED(HttpStatus.UNAUTHORIZED),
    INVALID_AUTHORIZATION(HttpStatus.UNAUTHORIZED),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus status;

}
