package com.scheduling.api.service.impl;

import com.scheduling.api.domain.Appointment;
import com.scheduling.api.domain.Service;
import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.dvo.Schedule;
import com.scheduling.api.domain.enumerates.WeekDays;
import com.scheduling.api.infra.errors.bussines.NotFoundRecordException;
import com.scheduling.api.infra.providers.ClockProvider;
import com.scheduling.api.repositories.AppointmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository repository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    @DisplayName("should confirm pending appointment")
    void shouldConfirmAppointmentWhenItExists() {
        var appointment = this.createPendingAppointment();

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(appointment));

        this.appointmentService.confirm(1L);

        assertTrue(appointment.isConfirmed());

        verify(this.repository, times(1)).findById(1L);
        verify(this.repository, times(1)).save(same(appointment));
    }

    @Test
    @DisplayName("should throw not found in confirm where appointment not exists")
    void shouldThrowNotFoundInConfirmWhereAppointmentNotExists() {
        when(this.repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundRecordException.class, () -> this.appointmentService.confirm(1L));

        verify(this.repository, times(1)).findById(1L);
        verify(this.repository, never()).save(any(Appointment.class));
    }


    private static class FakeTimer implements ClockProvider {
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

    private static final FakeTimer CLOCK_PROVIDER = new FakeTimer();
    private static final DayHour NOW = new DayHour(CLOCK_PROVIDER.currentDate(), CLOCK_PROVIDER.currentTime());

    private Appointment createPendingAppointment() {
        return this.createService()
                .schedule(DayHour.now(CLOCK_PROVIDER), DayHour.now(CLOCK_PROVIDER));
    }

    private Service createService() {
        return Service.builder()
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
    }

}