package com.scheduling.api.domain;

import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.dvo.Schedule;
import com.scheduling.api.domain.enumerates.WeekDays;
import com.scheduling.api.domain.exceptions.DomainException;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="tb_offered_services")
public class OfferedService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "offeredService", cascade = CascadeType.REMOVE)
    private final Set<ServiceWorkDay> workDays;
    @OneToMany(mappedBy = "offeredService", cascade = CascadeType.REMOVE)
    private final Set<ServiceSchedules> schedules;

    public OfferedService(
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


    public static OfferedServiceBuilder builder() {
        return new OfferedServiceBuilder();
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
    public static class OfferedServiceBuilder {

        private String name;
        private String description;
        private final Set<ServiceWorkDay> weekDaysWork;
        private final Set<ServiceSchedules> schedulesOfService;

        public OfferedServiceBuilder() {
            this.name = null;
            this.description = null;
            this.weekDaysWork =  new HashSet<>();
            this.schedulesOfService = new HashSet<>();
        }

        public OfferedServiceBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public OfferedServiceBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public OfferedServiceBuilder addSchedule(Schedule time) {
            this.schedulesOfService.add(new ServiceSchedules(time));
            return this;
        }

        public OfferedServiceBuilder addWorkDay(WeekDays weekDays) {
            this.weekDaysWork.add(new ServiceWorkDay(weekDays));
            return this;
        }

        public OfferedService build() {
            return new OfferedService(this.name, this.description, this.weekDaysWork, this.schedulesOfService);
        }
    }

}
