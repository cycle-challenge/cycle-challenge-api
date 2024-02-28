package com.yeohangttukttak.api.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiErrorResponse {

    private List<ApiError> errors;


}
