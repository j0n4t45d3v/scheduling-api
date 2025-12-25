package com.scheduling.api.domain.exceptions.appointment;

import com.scheduling.api.domain.exceptions.DomainException;

public class AppointmentCannotBeRejectedException extends DomainException {
    public AppointmentCannotBeRejectedException() {
        super("Appointment is cannot be reject");
    }
}
