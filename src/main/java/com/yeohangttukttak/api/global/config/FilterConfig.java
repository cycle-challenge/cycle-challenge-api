package com.yeohangttukttak.api.global.config;

import com.yeohangttukttak.api.domain.member.dao.RefreshTokenRepository;
import com.yeohangttukttak.api.domain.member.service.TokenService;
import com.yeohangttukttak.api.global.config.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> registrationBean() {
        FilterRegistrationBean<JwtAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtAuthFilter(tokenService, refreshTokenRepository));
        registration.addUrlPatterns("/api/*"); // 이 필터가 적용될 URL 패턴
        return registration;
    }
}
