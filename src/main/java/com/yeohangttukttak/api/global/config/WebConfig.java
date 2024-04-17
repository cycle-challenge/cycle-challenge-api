package com.yeohangttukttak.api.global.config;

import com.yeohangttukttak.api.global.config.converter.StringToLocationDTOConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToLocationDTOConverter());
    }

}
