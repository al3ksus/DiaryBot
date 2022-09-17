package com.example.DiaryBot.service;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Schedule;
import com.example.DiaryBot.model.enums.DayOfWeek;
import com.example.DiaryBot.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public void addSchedule(DayOfWeek dayOfWeek, Chat chat) {
        scheduleRepository.save(new Schedule(dayOfWeek, chat));
    }

    public Optional<Schedule> findWithoutText() {
        return scheduleRepository.findByText(null);
    }

    public void setText(Schedule schedule, String text) {
        schedule.setText(text);
        scheduleRepository.save(schedule);
    }
}
