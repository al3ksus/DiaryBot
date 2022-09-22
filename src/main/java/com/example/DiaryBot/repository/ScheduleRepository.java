package com.example.DiaryBot.repository;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Schedule;
import com.example.DiaryBot.model.enums.DayOfWeek;
import com.example.DiaryBot.model.enums.ScheduleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByChatAndScheduleState(Chat chat, ScheduleState scheduleState);

    Optional<Schedule> findByChatAndDayOfWeek(Chat chat, DayOfWeek dayOfWeek);
}
