package com.scheduling.api.domain;

import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.enumerates.WeekDays;

public class ServiceWorkDay {

    private WeekDays weekDay;

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
