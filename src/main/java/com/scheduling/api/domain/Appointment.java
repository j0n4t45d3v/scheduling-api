package com.scheduling.api.domain;

import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.exceptions.appointment.AppointmentIsNotConfirmedException;
import com.scheduling.api.domain.exceptions.appointment.AppointmentCannotBeConfirmedException;
import com.scheduling.api.domain.exceptions.appointment.AppointmentCannotBeRejectedException;
import com.scheduling.api.domain.exceptions.appointment.AppointmentReasonIsRequiredException;
import jakarta.persistence.*;

import java.util.Optional;

@Entity
@Table(name="tb_appointments")
public class Appointment {

    public enum Status {
        PENDING,
        CONFIRMED,
        REJECTED,
        CANCELED;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "day", column = @Column(name = "day_schedule")),
            @AttributeOverride(name = "hour", column = @Column(name = "hour_schedule")),
    })
    private final DayHour dayHour;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String reason;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private final OfferedService offeredService;

    protected Appointment(DayHour dayHour, OfferedService offeredService) {
        this.dayHour = dayHour;
        this.status = Status.PENDING;
        this.reason = null;
        this.offeredService = offeredService;
    }

    public Long getId() {
        return this.id;
    }

    public void confirm() {
        if (!this.isPending()) {
            throw new AppointmentCannotBeConfirmedException();
        }
        this.status = Status.CONFIRMED;
    }

    public void cancel(String reason) {
        if (!this.isConfirmed()) {
            throw new AppointmentIsNotConfirmedException();
        }
        this.reason = reason;
        this.status = Status.CANCELED;
    }

    public void reject(String reason) {
        if (!this.isPending()) {
            throw new AppointmentCannotBeRejectedException();
        }
        if (reason == null || reason.isBlank()) {
            throw new AppointmentReasonIsRequiredException();
        }
        this.reason = reason;
        this.status = Status.REJECTED;
    }

    public boolean isConfirmed() {
        return this.status.equals(Status.CONFIRMED);
    }

    public boolean isPending() {
        return this.status.equals(Status.PENDING);
    }

    public boolean isCanceled() {
        return this.status.equals(Status.CANCELED);
    }

    public boolean isRejected() {
        return this.status.equals(Status.REJECTED);
    }

    public Optional<String> getReason() {
        return Optional.ofNullable(reason);
    }
}
