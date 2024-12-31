package com.tubes.pbw.user.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    // Define the expected input format for the date string
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    public LocalDateTime convert(String source) {
        try {
            return LocalDateTime.parse(source, formatter); // Parse the string into LocalDateTime
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid datetime format. Please use yyyy-MM-dd'T'HH:mm.");
        }
    }
}
