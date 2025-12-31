package com.scheduling.api.service.impl;

import com.scheduling.api.stubs.TestClockProvider;
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

    private static final ClockProvider CLOCK_PROVIDER = TestClockProvider.INSTANCE;
    private static final DayHour NOW = DayHour.now(CLOCK_PROVIDER);

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

    @Test
    @DisplayName("should reject pending appointment")
    void shouldRejectPendingAppointment() {
        var appointment = this.createPendingAppointment();

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(appointment));

        this.appointmentService.reject(1L, "Test reject");

        assertTrue(appointment.isRejected());

        verify(this.repository, times(1)).findById(1L);
        verify(this.repository, times(1)).save(same(appointment));
    }

    @Test
    @DisplayName("should throw not found in reject when appointment not exists")
    void shouldThrowNotFoundInRejectWhenAppointmentNotExists() {
        when(this.repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundRecordException.class, () -> this.appointmentService.reject(1L, "Test reject"));

        verify(this.repository, times(1)).findById(1L);
        verify(this.repository, never()).save(any(Appointment.class));
    }

    @Test
    @DisplayName("should cancel confirmed appointment")
    void shouldCancelConfirmedAppointment() {
        var appointment = this.createPendingAppointment();
        appointment.confirm();

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(appointment));

        this.appointmentService.cancel(1L, "Test reject");

        assertTrue(appointment.isCanceled());

        verify(this.repository, times(1)).findById(1L);
        verify(this.repository, times(1)).save(same(appointment));
    }

    @Test
    @DisplayName("should throw not found in cancel when appointment not exists")
    void shouldThrowNotFoundInCancelWhenAppointmentNotExists() {
        when(this.repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundRecordException.class, () -> this.appointmentService.cancel(1L, "Test reject"));

        verify(this.repository, times(1)).findById(1L);
        verify(this.repository, never()).save(any(Appointment.class));
    }

    private Appointment createPendingAppointment() {
        return this.createService()
                .schedule(NOW, NOW);
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