package com.scheduling.api.domain;

import com.scheduling.api.domain.dvo.Schedule;

import java.time.LocalTime;
import java.util.Set;

public class ServiceSchedules {

    private final Schedule schedule;

    public ServiceSchedules(Schedule schedule) {
        this.schedule = schedule;
    }

    public boolean isAvailable(LocalTime time) {
        return this.schedule.isEquals(time);
    }

}
