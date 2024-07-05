package com.example.DiaryBot.service;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Schedule;
import com.example.DiaryBot.model.enums.DayOfWeek;
import com.example.DiaryBot.model.enums.ScheduleState;
import com.example.DiaryBot.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void addSchedule(Chat chat, DayOfWeek dayOfWeek) {
        scheduleRepository.save(new Schedule(chat, dayOfWeek));
    }

    @Override
    public void addSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    @Override
    public Optional<Schedule> getSchedule(Long id) {
        return scheduleRepository.findById(id);
    }

    @Override
    public Optional<Schedule> findByDay(Chat chat, DayOfWeek dayOfWeek) {
        return scheduleRepository.findByChatAndDayOfWeek(chat, dayOfWeek);
    }

    @Override
    public Optional<Schedule> findByState(Chat chat, ScheduleState scheduleState) {
        return scheduleRepository.findByChatAndScheduleState(chat, scheduleState);
    }

    @Override
    public void setText(Schedule schedule, String text) {
        schedule.setText(text);
        scheduleRepository.save(schedule);
    }

    @Override
    public void setState(Schedule schedule, ScheduleState scheduleState) {
        schedule.setScheduleState(scheduleState);
        scheduleRepository.save(schedule);
    }

    @Override
    public void delete(Schedule schedule) {
        scheduleRepository.delete(schedule);
    }
}
