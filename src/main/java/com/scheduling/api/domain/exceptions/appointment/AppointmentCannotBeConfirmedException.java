package com.scheduling.api.domain.exceptions.appointment;

import com.scheduling.api.domain.exceptions.DomainException;

public class AppointmentCannotBeConfirmedException extends DomainException {
    public AppointmentCannotBeConfirmedException() {
        super("Appointment is not pending confirmation");
    }
}
