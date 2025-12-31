package com.scheduling.api.service;

import com.scheduling.api.domain.Appointment;

public interface AppointmentService {

    Appointment confirm(Long id);

    Appointment reject(Long id, String rejectReason);

    Appointment cancel(Long id, String cancelReason);

}
