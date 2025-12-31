package com.scheduling.api.infra.providers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface ClockProvider {
    LocalDateTime now();
    LocalDate currentDate();
    LocalTime currentTime();
}
