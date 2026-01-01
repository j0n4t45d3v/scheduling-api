package com.scheduling.api.domain;

import com.scheduling.api.domain.dvo.Schedule;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "tb_services_schedules")
public class ServiceSchedules {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "schedule")))
    private final Schedule schedule;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    public ServiceSchedules(Schedule schedule) {
        this.schedule = schedule;
    }

    public boolean isAvailable(LocalTime time) {
        return this.schedule.isEquals(time);
    }

}
