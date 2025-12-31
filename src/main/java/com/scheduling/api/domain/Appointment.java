package com.scheduling.api.domain;

import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.exceptions.appointment.AppointmentIsNotConfirmedException;
import com.scheduling.api.domain.exceptions.appointment.AppointmentCannotBeConfirmedException;
import com.scheduling.api.domain.exceptions.appointment.AppointmentCannotBeRejectedException;
import com.scheduling.api.domain.exceptions.appointment.AppointmentReasonIsRequiredException;

import java.util.Optional;

public class Appointment {

    public enum Status {
        PENDING,
        CONFIRMED,
        REJECTED,
        CANCELED;
    }

    private Long id;
    private final DayHour dayHour;
    private Status status;
    private String reason;
    private final Service service;

    public Appointment(DayHour dayHour, Service service) {
        this.dayHour = dayHour;
        this.status = Status.PENDING;
        this.reason = null;
        this.service = service;
    }

    public void confirm() {
        if (!this.isPending()) {
            throw new AppointmentCannotBeConfirmedException();
        }
        this.status = Status.CONFIRMED;
    }

    public void cancel(String reason) {
        if (!this.isConfirmed()) {
            throw new AppointmentIsNotConfirmedException();
        }
        this.reason = reason;
        this.status = Status.CANCELED;
    }

    public void reject(String reason) {
        if (!this.isPending()) {
            throw new AppointmentCannotBeRejectedException();
        }
        if (reason == null || reason.isBlank()) {
            throw new AppointmentReasonIsRequiredException();
        }
        this.reason = reason;
        this.status = Status.REJECTED;
    }

    public boolean isConfirmed() {
        return this.status.equals(Status.CONFIRMED);
    }

    public boolean isPending() {
        return this.status.equals(Status.PENDING);
    }

    public boolean isCanceled() {
        return this.status.equals(Status.CANCELED);
    }

    public boolean isReject() {
        return this.status.equals(Status.REJECTED);
    }

    public Optional<String> getReason() {
        return Optional.ofNullable(reason);
    }
}
