package com.scheduling.api.domain.dvo;

import com.scheduling.api.domain.enumerates.WeekDays;
import com.scheduling.api.infra.providers.ClockProvider;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;

public record DayHour(LocalDate day, LocalTime hour) {

    public DayHour {
        if (day == null|| hour == null) {
            throw new RuntimeException("not provider day/hour");
        }
    }

    public static DayHour now(ClockProvider provider) {
        return new DayHour(provider.currentDate(), provider.currentTime());
    }

    public boolean isBefore(DayHour other) {
        return this.dayIsBefore(other) || this.hourIsBefore(other);
    }

    public boolean dayIsBefore(DayHour other) {
        return this.day.isBefore(other.day());
    }

    public boolean hourIsBefore(DayHour other) {
        return this.hour.isBefore(other.hour());
    }

    public boolean isEqualsTo(DayHour dayHour) {
        return this.day.isEqual(ChronoLocalDate.from(dayHour.day()))
                && this.hour.equals(dayHour.hour());
    }

    public WeekDays getDayOfWeek() {
        return WeekDays.of(this.day.getDayOfWeek());
    }

}
