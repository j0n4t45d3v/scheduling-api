package com.scheduling.api.domain;

import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.enumerates.WeekDays;
import jakarta.persistence.*;

@Embeddable
public class ServiceWorkDay {

    @Enumerated(EnumType.STRING)
    private WeekDays weekDay;

    protected ServiceWorkDay() {}

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
