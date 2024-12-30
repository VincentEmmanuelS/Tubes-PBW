package com.tubes.pbw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.tubes.pbw.component.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToLocalDateTimeConverter());
        registry.addConverter(new StringToLocalTimeConverter());
        // registry.addFormatter(new LocalDateTimeFormatter());
    }
}
