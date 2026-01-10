package com.scheduling.api.domain.dvo;

import com.scheduling.api.infra.providers.ClockProvider;
import com.scheduling.api.stubs.TestClockProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class DayHourTest {

    private static final ClockProvider CLOCK_PROVIDER = TestClockProvider.INSTANCE;

    @Test
    @DisplayName("should create dayHour only when given date and time")
    void shouldCreateDayHourOnlyWhenGivenDateAndTime() {
        assertDoesNotThrow(() -> new DayHour(LocalDate.now(), LocalTime.now()));
    }

    @Test
    @DisplayName("should not allow create DayHour when not given date")
    void shouldNotAllowCreateDayHourWhenNotGivenDate() {
        assertThrows(IllegalArgumentException.class, () -> new DayHour(null, LocalTime.now()));
    }

    @Test
    @DisplayName("should not allow create DayHour when not given time")
    void shouldNotAllowCreateDayHourWhenNotGivenTime() {
        assertThrows(IllegalArgumentException.class, () -> new DayHour(LocalDate.now(), null));
    }

    @Test
    @DisplayName("should get date and time current given clock provider")
    void shouldGetDateAndTimeCurrentGivenClockProvider() {
        DayHour dayHour = DayHour.now(CLOCK_PROVIDER);
        assertEquals(CLOCK_PROVIDER.currentDate(), dayHour.day());
        assertEquals(CLOCK_PROVIDER.currentTime(), dayHour.hour());
    }

    @Test
    @DisplayName("should return true when date is before")
    void shouldReturnTrueWhenDateIsBefore() {
        LocalTime hour = LocalTime.of(1, 30);
        DayHour dayHourYesterday = new DayHour(LocalDate.of(1999, 12, 11), hour);
        DayHour dayHour = new DayHour(LocalDate.of(1999, 12, 12), hour);
        assertTrue(dayHourYesterday.isBefore(dayHour));
    }

    @Test
    @DisplayName("should return true when hour is before")
    void shouldReturnTrueWhenHourIsBefore() {
        LocalDate day = LocalDate.of(1999, 12, 12);
        DayHour dayHour = new DayHour(day, LocalTime.of(1, 30));
        DayHour dayHourWithHourMinusFiveMinutes = new DayHour(day, LocalTime.of(1, 25));
        assertTrue(dayHourWithHourMinusFiveMinutes.isBefore(dayHour));
    }

    @Test
    @DisplayName("should return false when hour is before and day is after")
    void shouldReturnFalseWhenHourIsBeforeAndDayIsAfter() {
        LocalDate day = LocalDate.of(1999, 12, 12);
        LocalDate dayTomorrow = LocalDate.of(1999, 12, 13);
        DayHour dayHour = new DayHour(day, LocalTime.of(1, 30));
        DayHour tomorrowWithHourMinusFiveMinutes = new DayHour(dayTomorrow, LocalTime.of(1, 25));
        assertFalse(tomorrowWithHourMinusFiveMinutes.isBefore(dayHour));
    }

    @Test
    @DisplayName("should return true when DayHour is equals")
    void shouldReturnTrueWhenDayHourIsEquals() {
        LocalDate day = LocalDate.of(1999, 12, 12);
        LocalTime hour = LocalTime.of(1, 30);
        DayHour dayHour1 = new DayHour(day, hour);
        DayHour dayHour2 = new DayHour(day, hour);
        assertTrue(dayHour1.isEqualsTo(dayHour2));
    }

    @Test
    @DisplayName("should return false when date is not equals to DayHour")
    void shouldReturnFalseWhenDayIsNotEqualsToDayHour() {
        LocalDate day1 = LocalDate.of(1999, 12, 12);
        LocalDate day2 = LocalDate.of(1999, 12, 13);
        LocalTime hour = LocalTime.of(1, 30);
        DayHour dayHour1 = new DayHour(day1, hour);
        DayHour dayHour2 = new DayHour(day2, hour);
        assertFalse(dayHour1.isEqualsTo(dayHour2));
    }

    @Test
    @DisplayName("should return false when time is not equals to DayHour")
    void shouldReturnFalseWhenTimeIsNotEqualsToDayHour() {
        LocalDate day = LocalDate.of(1999, 12, 12);
        LocalTime hour1 = LocalTime.of(1, 30);
        LocalTime hour2 = LocalTime.of(2, 30);
        DayHour dayHour1 = new DayHour(day, hour1);
        DayHour dayHour2 = new DayHour(day, hour2);
        assertFalse(dayHour1.isEqualsTo(dayHour2));
    }


}