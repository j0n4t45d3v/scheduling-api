package com.scheduling.api.service;

import com.scheduling.api.domain.Appointment;
import com.scheduling.api.domain.dvo.DayHour;

public interface SchedulingService {

    Appointment schedule(Long serviceId, DayHour appointment);

}
