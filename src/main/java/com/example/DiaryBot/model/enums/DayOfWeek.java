package com.example.DiaryBot.model.enums;

public enum DayOfWeek {

    MONDAY (1, "понедельник"),
    TUESDAY (2, "вторник"),
    WEDNESDAY (3, "среду"),
    THURSDAY (4, "четверг"),
    FRIDAY (5, "пятницу"),
    SATURDAY (6, "субботу"),
    SUNDAY (7, "воскресенье");

    private final int number;

    private final String title;

    DayOfWeek(int number, String title) {
        this.number = number;
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }
}
