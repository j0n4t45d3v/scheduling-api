package com.scheduling.api.service.impl;

import com.scheduling.api.domain.Appointment;
import com.scheduling.api.domain.OfferedService;
import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.dvo.Schedule;
import com.scheduling.api.domain.enumerates.WeekDays;
import com.scheduling.api.infra.errors.bussines.NotFoundRecordException;
import com.scheduling.api.infra.providers.ClockProvider;
import com.scheduling.api.repositories.AppointmentRepository;
import com.scheduling.api.repositories.OfferedServiceRepository;
import com.scheduling.api.stubs.TestClockProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceImplTest {

    @Mock
    private OfferedServiceRepository repository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Spy
    private ClockProvider clockProvider = TestClockProvider.INSTANCE;
    @InjectMocks
    private SchedulingServiceImpl schedulingService;

    @Test
    @DisplayName("should schedule service")
    void shouldScheduleService() {
        var service = OfferedService.builder()
                .setName("Test")
                .setDescription("Test description")
                .addSchedule(new Schedule(this.clockProvider.currentTime()))
                .addWorkDay(WeekDays.SUNDAY)
                .addWorkDay(WeekDays.MONDAY)
                .addWorkDay(WeekDays.THURSDAY)
                .addWorkDay(WeekDays.WEDNESDAY)
                .addWorkDay(WeekDays.TUESDAY)
                .addWorkDay(WeekDays.FRIDAY)
                .addWorkDay(WeekDays.SATURDAY)
                .build();

        var now = DayHour.now(this.clockProvider);

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(service));

        var appointment = this.schedulingService.schedule(1L, now);

        assertNotNull(appointment);
        assertTrue(appointment.isPending());

        verify(this.repository, times(1)).findById(anyLong());
        verify(this.appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    @DisplayName("should throw not found record when service provide not exists")
    void shouldThrowNotFoundRecordWhenServiceProvideNotExists() {
        var now = DayHour.now(this.clockProvider);

        when(this.repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundRecordException.class, () -> this.schedulingService.schedule(1L, now));

        verify(this.repository, times(1)).findById(anyLong());
        verify(this.appointmentRepository, never()).save(any(Appointment.class));
    }

}