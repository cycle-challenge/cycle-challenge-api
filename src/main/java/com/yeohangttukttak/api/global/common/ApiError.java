package com.yeohangttukttak.api.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@AllArgsConstructor
@Getter
@JsonInclude(NON_NULL)
public class ApiError {

    private String message;

    private String target;


}
