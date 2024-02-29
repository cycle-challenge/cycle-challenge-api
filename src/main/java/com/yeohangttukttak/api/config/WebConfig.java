package com.yeohangttukttak.api.config;

import com.yeohangttukttak.api.converter.StringToLocationDTOConverter;
import com.yeohangttukttak.api.converter.StringToValueBasedEnumConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToLocationDTOConverter());
        registry.addConverterFactory(new StringToValueBasedEnumConverter());
    }

}
