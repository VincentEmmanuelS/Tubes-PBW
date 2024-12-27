package com.tubes.pbw.component;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

    // Updated pattern to include the day of the week
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, h:mm a");

    @Override
    public LocalDateTime parse(String text, Locale locale) {
        return LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME); // Parsing (not required for output)
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        return object.format(formatter); // Format LocalDateTime with the day
    }
}
