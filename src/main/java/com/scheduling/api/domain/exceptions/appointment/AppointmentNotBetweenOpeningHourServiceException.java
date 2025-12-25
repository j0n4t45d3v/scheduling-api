package com.scheduling.api.domain.exceptions.appointment;

import com.scheduling.api.domain.exceptions.DomainException;

public class AppointmentNotBetweenOpeningHourServiceException extends DomainException {

    public AppointmentNotBetweenOpeningHourServiceException() {
        super("Day/hour not between date time service work");
    }

}
