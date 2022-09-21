package com.example.DiaryBot.service;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Schedule;
import com.example.DiaryBot.model.enums.DayOfWeek;
import com.example.DiaryBot.model.enums.ScheduleState;
import com.example.DiaryBot.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public void addSchedule(Chat chat, DayOfWeek dayOfWeek) {
        scheduleRepository.save(new Schedule(chat, dayOfWeek));
    }

    public Optional<Schedule> getSchedule(Long id) {
        return scheduleRepository.findById(id);
    }

    public Optional<Schedule> findByDay(Chat chat, DayOfWeek dayOfWeek) {
        return scheduleRepository.findByChatAndDayOfWeek(chat, dayOfWeek);
    }

    public Optional<Schedule> findByState(Chat chat, ScheduleState scheduleState) {
        return scheduleRepository.findByChatAndScheduleState(chat, scheduleState);
    }

    public void setText(Schedule schedule, String text) {
        schedule.setText(text);
        scheduleRepository.save(schedule);
    }

    public void setState(Schedule schedule, ScheduleState scheduleState) {
        schedule.setScheduleState(scheduleState);
        scheduleRepository.save(schedule);
    }
}
