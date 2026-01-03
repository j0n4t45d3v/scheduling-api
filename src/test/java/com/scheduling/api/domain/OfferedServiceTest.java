package com.scheduling.api.domain;

import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.dvo.Schedule;
import com.scheduling.api.domain.enumerates.WeekDays;
import com.scheduling.api.domain.exceptions.DomainException;
import com.scheduling.api.domain.exceptions.offeredservice.OfferedServiceInvalidException;
import com.scheduling.api.infra.providers.ClockProvider;
import com.scheduling.api.stubs.TestClockProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class OfferedServiceTest {

    private static final ClockProvider CLOCK_PROVIDER = TestClockProvider.INSTANCE;
    private static final DayHour NOW = new DayHour(CLOCK_PROVIDER.currentDate(), CLOCK_PROVIDER.currentTime());

    @Test
    @DisplayName("should not allow create service when no name was given")
    void shouldNotAllowCreateServiceWhenNoNameWasGiven() {
        var offeredServiceBuilder = OfferedService.builder()
                .setDescription("Test description")
                .addSchedule(new Schedule(CLOCK_PROVIDER.currentTime()))
                .addWorkDay(WeekDays.SUNDAY);
        assertThrows(OfferedServiceInvalidException.class, offeredServiceBuilder::build);
    }

    @Test
    @DisplayName("should not allow create service when no description was given")
    void shouldNotAllowCreateServiceWhenNoDescriptionWasGiven() {
        var offeredServiceBuilder = OfferedService.builder()
                .setName("name")
                .addSchedule(new Schedule(CLOCK_PROVIDER.currentTime()))
                .addWorkDay(WeekDays.SUNDAY);
        assertThrows(OfferedServiceInvalidException.class, offeredServiceBuilder::build);
    }

    @Test
    @DisplayName("should not allow create service when no time slot was given")
    void shouldNotAllowCreateServiceWhenNoTimeSlotWasGiven() {
        var offeredServiceBuilder = OfferedService.builder()
                .setName("name")
                .setDescription("description")
                .addWorkDay(WeekDays.SUNDAY);
        assertThrows(OfferedServiceInvalidException.class, offeredServiceBuilder::build);
    }

    @Test
    @DisplayName("should not allow create service when no work day was given")
    void shouldNotAllowCreateServiceWhenNoWorkDayWasGiven() {
        var offeredServiceBuilder = OfferedService.builder()
                .setName("name")
                .setDescription("description")
                .addSchedule(new Schedule(CLOCK_PROVIDER.currentTime()));
        assertThrows(OfferedServiceInvalidException.class, offeredServiceBuilder::build);
    }


    @Test
    @DisplayName("should allow scheduling service when date passed is not in past")
    void shouldAllowSchedulingServiceWhenDatePassedIsNotInPast() {
        var service = OfferedService.builder()
                .setName("Test")
                .setDescription("Test description")
                .addSchedule(new Schedule(CLOCK_PROVIDER.currentTime()))
                .addWorkDay(WeekDays.SUNDAY)
                .addWorkDay(WeekDays.MONDAY)
                .addWorkDay(WeekDays.THURSDAY)
                .addWorkDay(WeekDays.WEDNESDAY)
                .addWorkDay(WeekDays.TUESDAY)
                .addWorkDay(WeekDays.FRIDAY)
                .addWorkDay(WeekDays.SATURDAY)
                .build();

        DayHour appointmentHour = new DayHour(
                LocalDate.of(1999, 12, 14),
                LocalTime.of(6, 0)
        );

        DayHour now = new DayHour(CLOCK_PROVIDER.currentDate(), CLOCK_PROVIDER.currentTime());
        var appointment = service.schedule(appointmentHour, now);
        assertNotNull(appointment);
    }

    @Test
    @DisplayName("should not allow scheduling service when it is in past")
    void shouldAllowSchedulingServiceWhenItIsInPast() {
        var service = OfferedService.builder()
                .setName("Test")
                .setDescription("Test description")
                .addSchedule(new Schedule(CLOCK_PROVIDER.currentTime()))
                .addWorkDay(WeekDays.SUNDAY)
                .addWorkDay(WeekDays.MONDAY)
                .addWorkDay(WeekDays.THURSDAY)
                .addWorkDay(WeekDays.WEDNESDAY)
                .addWorkDay(WeekDays.TUESDAY)
                .addWorkDay(WeekDays.FRIDAY)
                .addWorkDay(WeekDays.SATURDAY)
                .build();

        DayHour appointmentHourCaseTimeInPast = new DayHour(
                LocalDate.of(1999, 12, 12),
                LocalTime.of(5, 0)
        );

        DayHour now = new DayHour(CLOCK_PROVIDER.currentDate(), CLOCK_PROVIDER.currentTime());
        assertThrows(DomainException.class, () -> service.schedule(appointmentHourCaseTimeInPast, now));

        DayHour appointmentHourCaseDayInPast = new DayHour(
                LocalDate.of(1999, 12, 1),
                LocalTime.of(7, 0)
        );
        assertThrows(DomainException.class, () -> service.schedule(appointmentHourCaseDayInPast, now));

        DayHour appointmentHourCaseDayAndHoutInPast = new DayHour(
                LocalDate.of(1999, 12, 1),
                LocalTime.of(5, 0)
        );
        assertThrows(DomainException.class, () -> service.schedule(appointmentHourCaseDayAndHoutInPast, now));
    }

    @Test
    @DisplayName("should not allow scheduling service when it is not available on that weekday")
    void shouldNotAllowSchedulingServiceWhenItIsNotAvailableOnThatWeekDay() {
        var service = OfferedService.builder()
                .setName("Test")
                .setDescription("Test description")
                .addSchedule(new Schedule(CLOCK_PROVIDER.currentTime()))
                .addWorkDay(WeekDays.SUNDAY)
                .addWorkDay(WeekDays.MONDAY)
                .addWorkDay(WeekDays.WEDNESDAY)
                .build();

        DayHour appointmentHour = new DayHour(
                LocalDate.of(1999, 12, 14),
                LocalTime.of(6, 0)
        );
        assertThrows(DomainException.class, () -> service.schedule(appointmentHour, NOW));
    }

    @Test
    @DisplayName("should not allow scheduling service when it is not available on that hours")
    void shouldNotAllowSchedulingServiceWhenItIsNotAvailableOnThatHours() {
        var service = OfferedService.builder()
                .setName("Test")
                .setDescription("Test description")
                .addSchedule(new Schedule(CLOCK_PROVIDER.currentTime()))
                .addSchedule(new Schedule(CLOCK_PROVIDER.currentTime().plusHours(1)))
                .addSchedule(new Schedule(CLOCK_PROVIDER.currentTime().plusHours(2)))
                .addWorkDay(WeekDays.SUNDAY)
                .addWorkDay(WeekDays.MONDAY)
                .addWorkDay(WeekDays.THURSDAY)
                .addWorkDay(WeekDays.WEDNESDAY)
                .addWorkDay(WeekDays.TUESDAY)
                .addWorkDay(WeekDays.FRIDAY)
                .addWorkDay(WeekDays.SATURDAY)
                .build();

        DayHour appointmentHour = new DayHour(
                LocalDate.of(1999, 12, 14),
                LocalTime.of(10, 0)
        );

        assertThrows(DomainException.class, () -> service.schedule(appointmentHour, NOW));
    }

    @Test
    @DisplayName("should allow reschedule appointment only when missing at least 4 days to scheduled day")
    void shouldAllowRescheduleAppointmentOnlyWhenMissingAtLeast4DaysToScheduledDay() {
        var service = OfferedService.builder()
                .setName("Test")
                .setDescription("Test description")
                .addSchedule(new Schedule(CLOCK_PROVIDER.currentTime()))
                .addSchedule(new Schedule(CLOCK_PROVIDER.currentTime().plusHours(1)))
                .addSchedule(new Schedule(CLOCK_PROVIDER.currentTime().plusHours(2)))
                .addWorkDay(WeekDays.SUNDAY)
                .addWorkDay(WeekDays.MONDAY)
                .addWorkDay(WeekDays.THURSDAY)
                .addWorkDay(WeekDays.WEDNESDAY)
                .addWorkDay(WeekDays.TUESDAY)
                .addWorkDay(WeekDays.FRIDAY)
                .addWorkDay(WeekDays.SATURDAY)
                .build();

        Appointment appointment = new Appointment(NOW, service);
        Appointment appointmentRescheduled = service.reschedule(appointment, NOW);

        assertNotNull(appointmentRescheduled);
        assertTrue(appointment.isCanceled());
        var rescheduleReason = appointment.getReason();
        assertFalse(appointment.getReason().isEmpty());
        assertFalse(rescheduleReason.isEmpty());
        assertTrue(appointmentRescheduled.isPending());
    }


}