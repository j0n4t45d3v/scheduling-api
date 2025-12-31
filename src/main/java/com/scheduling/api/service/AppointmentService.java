package com.scheduling.api.service;

import com.scheduling.api.domain.Appointment;

public interface AppointmentService {

    Appointment confirm(Long id);

}
