package com.yeohangttukttak.api.global.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeohangttukttak.api.domain.member.dto.AuthRenewRequest;
import com.yeohangttukttak.api.domain.member.dto.TokenPayload;
import com.yeohangttukttak.api.domain.member.service.TokenService;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import com.yeohangttukttak.api.global.common.ModifiableHttpServletRequest;
import com.yeohangttukttak.api.global.util.ApiExceptionHandler;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.yeohangttukttak.api.global.util.HttpMessageReader.readBody;

@Slf4j
@RequiredArgsConstructor
public class JwtRefreshTokenFilter implements Filter {

    private final TokenService tokenService;
    private final ApiExceptionHandler exHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("[JWT_REFRESH_TOKEN_FILTER] init");
    }

    @Override
    public void destroy() {
        log.info("[JWT_REFRESH_TOKEN_FILTER] destroyed");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String contentType = httpRequest.getHeader("Content-Type");

        if (contentType == null || !contentType.contains("application/json")) {
            throw new ApiException(ApiErrorCode.AUTHORIZATION_REQUIRED);
        }

        String body = readBody(httpRequest);
        AuthRenewRequest authRenewRequest = objectMapper.readValue(body, AuthRenewRequest.class);

        try {
            TokenPayload payload = tokenService.decode(authRenewRequest.getRefreshToken());
            authRenewRequest.setEmail(payload.getEmail());

            chain.doFilter(new ModifiableHttpServletRequest(
                    httpRequest, objectMapper.writeValueAsString(authRenewRequest)), response);

        } catch(RuntimeException e) {
            exHandler.handle(e, httpRequest, httpResponse);
        }

    }


}
