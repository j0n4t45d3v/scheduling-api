package com.scheduling.api.domain;

import com.scheduling.api.domain.dvo.Schedule;
import jakarta.persistence.*;

import java.time.LocalTime;

@Embeddable
public class ServiceSchedules {

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "schedule")))
    private Schedule schedule;

    protected ServiceSchedules() {}

    public ServiceSchedules(Schedule schedule) {
        this.schedule = schedule;
    }

    public boolean isAvailable(LocalTime time) {
        return this.schedule.isEquals(time);
    }

}
