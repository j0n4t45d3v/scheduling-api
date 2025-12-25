package com.scheduling.api.domain;

import com.scheduling.api.domain.dvo.Schedule;
import com.scheduling.api.domain.exceptions.DomainException;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class Service {

    private String name;
    private String description;
    private LocalTime duranteService;
    private Set<ServiceSchedules> schedules;

    public Service() {
        this(null, null, null, new HashSet<>());
    }

    public Service(
            String name,
            String description,
            LocalTime duranteService,
            Set<ServiceSchedules> schedules
    ) {
        this.name = name;
        this.description = description;
        this.duranteService = duranteService;
        this.schedules = schedules;

        if (this.schedules == null || this.schedules.isEmpty()) {
            throw new DomainException("Provider one time range to opening hour");
        }
    }


    public static ServiceBuilder builder() {
        return new ServiceBuilder();
    }


    public static class ServiceBuilder {

        private String name;
        private String description;
        private LocalTime duranteService;
        private final Set<ServiceSchedules> schedulesOfService;

        public ServiceBuilder() {
            this.name = null;
            this.description = null;
            this.duranteService = null;
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

        public ServiceBuilder setDuranteService(LocalTime duranteService) {
            this.duranteService = duranteService;
            return this;
        }

        public ServiceBuilder addSchedule(Schedule time) {
            this.schedulesOfService.add(new ServiceSchedules(time));
            return this;
        }

        public Service build() {
            return new Service(this.name, this.description, this.duranteService, this.schedulesOfService);
        }
    }

}
