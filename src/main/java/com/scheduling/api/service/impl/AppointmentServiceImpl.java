package com.scheduling.api.service.impl;

import com.scheduling.api.domain.Appointment;
import com.scheduling.api.infra.errors.bussines.NotFoundRecordException;
import com.scheduling.api.repositories.AppointmentRepository;
import com.scheduling.api.service.AppointmentService;

public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment confirm(Long id) {
        Appointment appointmentFound = this.appointmentRepository.findById(id)
                .orElseThrow( () -> new NotFoundRecordException("appointment not found"));
        appointmentFound.confirm();
        return this.appointmentRepository.save(appointmentFound);
    }
}
