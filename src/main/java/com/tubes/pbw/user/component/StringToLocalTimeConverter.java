package com.tubes.pbw.user.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class StringToLocalTimeConverter implements Converter<String, LocalTime> {

    private final DateTimeFormatter formatterWithSeconds = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter formatterWithoutSeconds = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public LocalTime convert(String source) {
        try {
            return LocalTime.parse(source, formatterWithSeconds);
        } catch (Exception e) {
            try {
                return LocalTime.parse(source, formatterWithoutSeconds);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Invalid time format. Please use HH:mm:ss or HH:mm.");
            }
        }
    }
}