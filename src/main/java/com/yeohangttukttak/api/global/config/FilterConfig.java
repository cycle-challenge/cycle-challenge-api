package com.yeohangttukttak.api.global.config;

import com.yeohangttukttak.api.global.config.filter.JwtAuthFilter;
import com.yeohangttukttak.api.global.config.filter.JwtRefreshTokenFilter;
import com.yeohangttukttak.api.global.util.ApiExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final ApiExceptionHandler exHandler;

    @Bean
    public FilterRegistrationBean<JwtRefreshTokenFilter> registerRefreshTokenFilter() {
        FilterRegistrationBean<JwtRefreshTokenFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtRefreshTokenFilter(exHandler));
        registration.addUrlPatterns("/api/v1/members/auth/renew");
        return registration;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> registrationBean() {
        FilterRegistrationBean<JwtAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtAuthFilter(exHandler));
        registration.addUrlPatterns("/api/*"); // 이 필터가 적용될 URL 패턴
        return registration;
    }

}
