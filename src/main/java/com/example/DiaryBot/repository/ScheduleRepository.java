package com.example.DiaryBot.repository;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByText(String text);

    Optional<Schedule> findByChatAndDayOfWeek(Chat chat, String dayOfWeek);
}
