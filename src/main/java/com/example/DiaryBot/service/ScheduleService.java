package com.example.DiaryBot.service;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Schedule;
import com.example.DiaryBot.model.enums.DayOfWeek;
import com.example.DiaryBot.model.enums.ScheduleState;

import java.util.Optional;

public interface ScheduleService {
    void addSchedule(Chat chat, DayOfWeek dayOfWeek);
    void addSchedule(Schedule schedule);
    Optional<Schedule> getSchedule(Long id);
    Optional<Schedule> findByDay(Chat chat, DayOfWeek dayOfWeek);
    Optional<Schedule> findByState(Chat chat, ScheduleState scheduleState);
    void setText(Schedule schedule, String text);
    void setState(Schedule schedule, ScheduleState scheduleState);
    void delete(Schedule schedule);
}
