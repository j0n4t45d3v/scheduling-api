package com.scheduling.api.domain.exceptions.appointment;

import com.scheduling.api.domain.exceptions.DomainException;

public class AppointmentCannotBeCanceledException extends DomainException {
    public AppointmentCannotBeCanceledException() {
        super("Appointment is not a confirmed");
    }
}
