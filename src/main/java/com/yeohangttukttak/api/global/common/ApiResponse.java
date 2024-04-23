package com.yeohangttukttak.api.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String status = "success";

    private String code;

    private T data;

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(ApiErrorCode errorCode, T data) {
        this.status = "fail";
        this.code = errorCode.name();
        this.data = data;
    }

}
