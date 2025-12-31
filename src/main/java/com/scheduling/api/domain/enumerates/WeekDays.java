package com.scheduling.api.domain.enumerates;

import java.time.DayOfWeek;

public enum WeekDays {
    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6);

    private final int index;

    WeekDays(int index) {
        this.index = index;
    }

    public static WeekDays of(DayOfWeek dayOfWeek) {
        return WeekDays.valueOf(dayOfWeek.name());
    }

    public int getIndex() {
        return index;
    }
}
