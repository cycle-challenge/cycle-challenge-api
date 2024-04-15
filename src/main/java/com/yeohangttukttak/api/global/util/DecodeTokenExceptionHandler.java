package com.yeohangttukttak.api.global.util;

import com.yeohangttukttak.api.domain.member.exception.TokenExpiredException;
import com.yeohangttukttak.api.domain.member.exception.TokenInvalidException;
import com.yeohangttukttak.api.global.common.ApiError;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


import static com.yeohangttukttak.api.global.util.HttpMessageSerializer.serialize;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Slf4j
public class DecodeTokenExceptionHandler {

    public static void handle(RuntimeException e, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        if (e instanceof TokenExpiredException) {
            serialize(httpResponse, SC_UNAUTHORIZED, new ApiError(
                    ApiErrorCode.AUTHORIZATION_EXPIRED, "인증 정보가 만료되었습니다.", null));
            return;
        }

        if (e instanceof TokenInvalidException) {
            log.error("""
                            [JWT_AUTH_FILTER] Caught! Credentials has been forged
                                - Uri: {}
                                - Host: {}
                                - User-Agent: {}""",
                    httpRequest.getRequestURI(),
                    httpRequest.getRemoteAddr(),
                    httpResponse.getHeader("User-Agent"));

            serialize(httpResponse, SC_UNAUTHORIZED, new ApiError(
                    ApiErrorCode.INVALIDED_AUTHORIZATION, "잘못된 인증 정보입니다.", null
            ));
            return;
        }

        serialize(httpResponse, SC_INTERNAL_SERVER_ERROR, new ApiError(
                ApiErrorCode.INTERNAL_SERVER_ERROR, "서버 오류", null
        ));
    }

}
