package com.example.DiaryBot.repository;

import com.example.DiaryBot.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    public Optional<Reminder> findByTime(String time);
}
