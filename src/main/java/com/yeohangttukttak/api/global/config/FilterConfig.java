package com.yeohangttukttak.api.global.config;

import com.yeohangttukttak.api.domain.member.dao.RefreshTokenRepository;
import com.yeohangttukttak.api.domain.member.service.TokenService;
import com.yeohangttukttak.api.global.config.filter.JwtAuthFilter;
import com.yeohangttukttak.api.global.config.filter.JwtRefreshTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final TokenService tokenService;

    @Bean
    public FilterRegistrationBean<JwtRefreshTokenFilter> registerRefreshTokenFilter() {
        FilterRegistrationBean<JwtRefreshTokenFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtRefreshTokenFilter(tokenService));
        registration.addUrlPatterns("/api/v1/members/renew");
        return registration;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> registrationBean() {
        FilterRegistrationBean<JwtAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtAuthFilter(tokenService));
        registration.addUrlPatterns("/api/*"); // 이 필터가 적용될 URL 패턴
        return registration;
    }

}
