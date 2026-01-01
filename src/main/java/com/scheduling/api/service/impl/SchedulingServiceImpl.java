package com.scheduling.api.service.impl;

import com.scheduling.api.domain.Appointment;
import com.scheduling.api.domain.Service;
import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.infra.errors.bussines.NotFoundRecordException;
import com.scheduling.api.infra.providers.ClockProvider;
import com.scheduling.api.repositories.AppointmentRepository;
import com.scheduling.api.repositories.ServiceRepository;
import com.scheduling.api.service.SchedulingService;

@org.springframework.stereotype.Service
public class SchedulingServiceImpl implements SchedulingService {

    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;
    private final ClockProvider clockProvider;

    public SchedulingServiceImpl(
            ServiceRepository serviceRepository,
            AppointmentRepository appointmentRepository,
            ClockProvider clockProvider
    ) {
        this.serviceRepository = serviceRepository;
        this.appointmentRepository = appointmentRepository;
        this.clockProvider = clockProvider;
    }

    @Override
    public Appointment schedule(Long serviceId, DayHour appointment) {
        Service service = this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundRecordException("service not found"));
        Appointment doneAppointment = service.schedule(appointment, DayHour.now(this.clockProvider));
        this.appointmentRepository.save(doneAppointment);
        return doneAppointment;
    }
}
