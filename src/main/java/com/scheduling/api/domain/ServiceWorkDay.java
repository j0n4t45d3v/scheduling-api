package com.scheduling.api.domain;

import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.enumerates.WeekDays;
import jakarta.persistence.*;

@Entity
@Table(name="tb_services_work_days")
public class ServiceWorkDay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private WeekDays weekDay;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private OfferedService offeredService;

    public ServiceWorkDay(WeekDays weekDay) {
        this.weekDay = weekDay;
    }

    public WeekDays getWeekDay() {
        return weekDay;
    }

    public boolean isAvailable(DayHour appointment) {
        return this.weekDay == appointment.getDayOfWeek();
    }
}
