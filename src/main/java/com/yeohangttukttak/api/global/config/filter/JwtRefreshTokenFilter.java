package com.yeohangttukttak.api.global.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeohangttukttak.api.domain.member.dto.AuthRenewRequest;
import com.yeohangttukttak.api.domain.member.dto.TokenPayload;
import com.yeohangttukttak.api.domain.member.exception.TokenExpiredException;
import com.yeohangttukttak.api.domain.member.exception.TokenInvalidException;
import com.yeohangttukttak.api.domain.member.service.TokenService;
import com.yeohangttukttak.api.global.common.ApiError;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ModifiableHttpServletRequest;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

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

        String uri = httpRequest.getRequestURI();
        String contentType = httpRequest.getHeader("Content-Type");

        if (contentType == null || !contentType.contains("application/json")) {
            chain.doFilter(request,response);
        }

        String body = readBody(httpRequest.getInputStream());
        AuthRenewRequest authRenewRequest = objectMapper.readValue(body, AuthRenewRequest.class);

        try {
            TokenPayload payload = tokenService.decode(authRenewRequest.getRefreshToken());
            authRenewRequest.setEmail(payload.getEmail());

            chain.doFilter(new ModifiableHttpServletRequest(
                    httpRequest, objectMapper.writeValueAsString(authRenewRequest)), response);

        } catch(RuntimeException e) {
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
                        uri, request.getRemoteAddr(), httpResponse.getHeader("User-Agent"));

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

    private void serialize(HttpServletResponse httpResponse, int status, Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            httpResponse.setStatus(status);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.getWriter().write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readBody(InputStream inputStream) {

        if (inputStream == null) return null;

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        try {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)  {
                sb.append(line);
            }

            return sb.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
