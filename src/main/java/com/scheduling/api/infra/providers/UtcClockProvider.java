package com.scheduling.api.infra.providers;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Component
public class UtcClockProvider implements ClockProvider {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    @Override
    public LocalDate currentDate() {
        return LocalDate.now();
    }

    @Override
    public LocalTime currentTime() {
        return LocalTime.now(ZoneId.of("UTC"));
    }
}
