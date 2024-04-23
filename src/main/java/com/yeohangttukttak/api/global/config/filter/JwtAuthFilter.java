package com.yeohangttukttak.api.global.config.filter;

import com.yeohangttukttak.api.domain.member.dto.TokenPayload;
import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import com.yeohangttukttak.api.global.util.ApiExceptionHandler;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter implements Filter {

    private final String[] whitelist = {
            "/api/v1/members/sign-in", "/api/v1/members/sign-up", "/api/v1/members/auth/renew",
            "/api/v1/places/nearby", "/api/v1/places/*/images", "/api/v1/travels/*/visits",
            "/api/v1/members/email/verify/send"
    };

    private final ApiExceptionHandler exHandler;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("[JWT_AUTH_FILTER] init");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.info("[JWT_AUTH_FILTER] destroyed");
    }

    @Override
    public void doFilter(
            ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            String uri = httpRequest.getRequestURI();

            // 1. Auth Filter 화이트 리스트 검사
            if (PatternMatchUtils.simpleMatch(whitelist, uri)) {
                chain.doFilter(request, response);
                return;
            }

            String token = parseToken(httpRequest);

            if (token == null)
                throw new ApiException(ApiErrorCode.INVALID_AUTHORIZATION);

            JwtToken accessToken = JwtToken.decode(token);
            httpRequest.setAttribute("accessToken", accessToken);

            chain.doFilter(request, response);

        } catch (RuntimeException e) {
            exHandler.handle(e, httpRequest, httpResponse);
        }
    }

    private String parseToken(HttpServletRequest httpRequest) {
        String bearerToken = httpRequest.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) return null;

        return bearerToken.substring(7);
    }
}
