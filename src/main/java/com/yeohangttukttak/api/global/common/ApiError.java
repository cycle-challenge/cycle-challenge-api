package com.yeohangttukttak.api.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@AllArgsConstructor
@Getter
@JsonInclude(NON_NULL)
public class ApiError {

    private int status;

    private String code;

    private String message;

    private String target;

    public ApiError(ApiErrorCode code, String message, String target) {
        this.status = code.getStatus().value();
        this.code = code.name();
        this.message = message;
        this.target = target;
    }

}
