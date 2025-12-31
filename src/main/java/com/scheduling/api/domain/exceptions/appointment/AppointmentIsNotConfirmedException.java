package com.scheduling.api.domain.exceptions.appointment;

import com.scheduling.api.domain.exceptions.DomainException;

public class AppointmentIsNotConfirmedException extends DomainException {
    public AppointmentIsNotConfirmedException() {
        super("Appointment is not confirmed");
    }
}
