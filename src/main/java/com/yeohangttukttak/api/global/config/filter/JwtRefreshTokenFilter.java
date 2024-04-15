package com.yeohangttukttak.api.global.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeohangttukttak.api.domain.member.dto.AuthRenewRequest;
import com.yeohangttukttak.api.domain.member.dto.TokenPayload;
import com.yeohangttukttak.api.domain.member.service.TokenService;
import com.yeohangttukttak.api.global.common.ModifiableHttpServletRequest;
import com.yeohangttukttak.api.global.util.DecodeTokenExceptionHandler;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.yeohangttukttak.api.global.util.HttpMessageReader.readBody;

@Slf4j
public class JwtRefreshTokenFilter implements Filter {

    private final TokenService tokenService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtRefreshTokenFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

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
            chain.doFilter(request,response);
        }

        String body = readBody(httpRequest);
        AuthRenewRequest authRenewRequest = objectMapper.readValue(body, AuthRenewRequest.class);

        try {
            TokenPayload payload = tokenService.decode(authRenewRequest.getRefreshToken());
            authRenewRequest.setEmail(payload.getEmail());

            chain.doFilter(new ModifiableHttpServletRequest(
                    httpRequest, objectMapper.writeValueAsString(authRenewRequest)), response);

        } catch(RuntimeException e) {
            DecodeTokenExceptionHandler
                    .handle(e, httpRequest, httpResponse);
        }

    }


}
