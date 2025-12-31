package com.scheduling.api.stubs;

import com.scheduling.api.infra.providers.ClockProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TestClockProvider implements ClockProvider {

    public static final TestClockProvider INSTANCE = new TestClockProvider();

    private TestClockProvider() {}

    @Override
    public LocalDateTime now() {
        return LocalDateTime.of(this.currentDate(), this.currentTime());
    }

    @Override
    public LocalDate currentDate() {
        return LocalDate.of(1999, 12, 12);
    }

    @Override
    public LocalTime currentTime() {
        return LocalTime.of(6, 0);
    }
}
