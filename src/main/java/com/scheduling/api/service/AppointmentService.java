package com.scheduling.api.service;

public interface AppointmentService {

    void confirm(Long id);

    void reject(Long id, String rejectReason);

    void cancel(Long id, String cancelReason);

}
