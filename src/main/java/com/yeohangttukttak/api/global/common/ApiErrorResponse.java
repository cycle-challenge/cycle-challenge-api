package com.yeohangttukttak.api.global.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiErrorResponse {

    private ApiErrorCode code;

    private List<ApiError> errors;

}
