package com.scheduling.api.domain.exceptions.appointment;

import com.scheduling.api.domain.exceptions.DomainException;

public class AppointmentReasonIsRequiredException extends DomainException {
    public AppointmentReasonIsRequiredException() {
        super("Reason is required");
    }
}
