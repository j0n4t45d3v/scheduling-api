package com.scheduling.api.domain;

import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.dvo.Schedule;
import com.scheduling.api.domain.enumerates.WeekDays;
import com.scheduling.api.domain.exceptions.appointment.AppointmentIsNotConfirmedException;
import com.scheduling.api.domain.exceptions.appointment.AppointmentCannotBeConfirmedException;
import com.scheduling.api.domain.exceptions.appointment.AppointmentCannotBeRejectedException;
import com.scheduling.api.domain.exceptions.appointment.AppointmentReasonIsRequiredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

    @Test
    @DisplayName("should create appointment like a pending")
    void shouldCreateAppointmentLikeAPending() {
        var newAppointment = this.createValidAppointment();
        assertTrue(newAppointment.isPending());
    }

    @Test
    @DisplayName("should confirm pending appointment")
    void shouldConfirmPendingAppointment() {
        var appointment = this.createValidAppointment();
        appointment.confirm();
        assertTrue(appointment.isConfirmed());
    }

    @Test
    @DisplayName("should not confirm appointment when it is not pending")
    void shouldNotBeConfirmedWhenAppointmentIsNotPending() {
        var appointment = this.createValidAppointment();
        appointment.confirm();
        assertThrows(AppointmentCannotBeConfirmedException.class, appointment::confirm);
    }

    @Test
    @DisplayName("should cancel confirmed appointment")
    void shouldCancelConfirmedAppointment() {
        var appointment = this.createValidAppointment();
        appointment.confirm();
        appointment.cancel(null);
        assertTrue(appointment.isCanceled());
    }

    @Test
    @DisplayName("should not cancel appointment when it is not confirmed")
    void shouldNotBeCanceledWhenAppointmentIsNotConfirmed() {
        var appointment = this.createValidAppointment();
        assertThrows(AppointmentIsNotConfirmedException.class, () -> appointment.cancel(null));
    }

    @Test
    @DisplayName("should allow rejecting appointment when it is pending")
    void shouldAllowRejectingAppointmentWhenItIsPending() {
        var appointment = this.createValidAppointment();
        appointment.reject("test reject");
        assertTrue(appointment.isRejected());
    }

    @Test
    @DisplayName("should not allow rejecting appointment when it is not pending")
    void shouldNotAllowRejectingAppointmentWhenItIsNotPending() {
        var appointment = this.createValidAppointment();
        appointment.confirm();
        assertThrows(AppointmentCannotBeRejectedException.class, () -> appointment.reject("test reject"));
    }

    @Test
    @DisplayName("should not allow rejecting when not provider reason")
    void shouldNotAllowRejectingWhenNotProviderReason() {
        var appointment = this.createValidAppointment();
        assertThrows(AppointmentReasonIsRequiredException.class, () -> appointment.reject(""), "reason is not be empty");
        assertThrows(AppointmentReasonIsRequiredException.class, () -> appointment.reject(null), "reason is not be null");
        assertThrows(AppointmentReasonIsRequiredException.class, () -> appointment.reject(" "), "reason is not be blank");
    }

    private Appointment createValidAppointment() {
        return new Appointment(
                new DayHour(LocalDate.now(), LocalTime.now(ZoneId.of("UTC")).plusHours(1)),
                this.createService()
        );
    }

    private Service createService() {
        return Service.builder()
                .setName("Test")
                .setDescription("Test description")
                .addSchedule(new Schedule(LocalTime.now(ZoneId.of("UTC"))))
                .addWorkDay(WeekDays.SUNDAY)
                .build();
    }

}