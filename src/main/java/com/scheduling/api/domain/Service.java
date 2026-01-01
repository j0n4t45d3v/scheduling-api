package com.scheduling.api.domain;

import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.dvo.Schedule;
import com.scheduling.api.domain.enumerates.WeekDays;
import com.scheduling.api.domain.exceptions.DomainException;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="tb_services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "service", cascade = CascadeType.REMOVE)
    private final Set<ServiceWorkDay> workDays;
    @OneToMany(mappedBy = "service", cascade = CascadeType.REMOVE)
    private final Set<ServiceSchedules> schedules;

    public Service(
            String name,
            String description,
            Set<ServiceWorkDay> workDays,
            Set<ServiceSchedules> schedules
    ) {
        if (name == null || name.isBlank()) {
            throw new DomainException("name is required");
        }
        if (description == null || description.isBlank()) {
            throw new DomainException("description is required");
        }
        if (workDays == null || workDays.isEmpty()) {
            throw new DomainException("provider one work day to service");
        }
        if (schedules == null || schedules.isEmpty()) {
            throw new DomainException("provider one schedule to service");
        }

        this.name = name;
        this.description = description;
        this.workDays = Set.copyOf(workDays);
        this.schedules = Set.copyOf(schedules);
    }


    public static ServiceBuilder builder() {
        return new ServiceBuilder();
    }

    public Appointment schedule(DayHour appointment, DayHour now) {
        if (appointment.isBefore(now)) {
            throw new DomainException("Cannot make an appointment in the past");
        }

        if (this.isNotAvailableForTheDayOfWeek(appointment)) {
            throw new DomainException("service is not available on " + appointment.getDayOfWeek().name());
        }

        if (this.isNotAvailableForSchedule(appointment)) {
            throw new DomainException("service is not available for the given schedule");
        }

        return new Appointment(appointment, this);
    }

    private boolean isNotAvailableForTheDayOfWeek(DayHour appointment) {
        return this.workDays
                .stream()
                .noneMatch(serviceWorkDay -> serviceWorkDay.isAvailable(appointment));
    }

    private boolean isNotAvailableForSchedule(DayHour appointment) {
        return this.schedules
                .stream()
                .noneMatch(schedules -> schedules.isAvailable(appointment.hour()));
    }

    public static class ServiceBuilder {

        private String name;
        private String description;
        private final Set<ServiceWorkDay> weekDaysWork;
        private final Set<ServiceSchedules> schedulesOfService;

        public ServiceBuilder() {
            this.name = null;
            this.description = null;
            this.weekDaysWork =  new HashSet<>();
            this.schedulesOfService = new HashSet<>();
        }

        public ServiceBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ServiceBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ServiceBuilder addSchedule(Schedule time) {
            this.schedulesOfService.add(new ServiceSchedules(time));
            return this;
        }

        public ServiceBuilder addWorkDay(WeekDays weekDays) {
            this.weekDaysWork.add(new ServiceWorkDay(weekDays));
            return this;
        }

        public Service build() {
            return new Service(this.name, this.description, this.weekDaysWork, this.schedulesOfService);
        }
    }

}
