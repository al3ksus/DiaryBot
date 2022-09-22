package com.example.DiaryBot.repository;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.enums.ReminderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    Optional<Reminder> findByChatAndReminderState(Chat chat, ReminderState reminderState);

    List<Reminder> findAllByChat(Chat chat);

    List<Reminder> findAllByChatAndReminderState(Chat chat, ReminderState reminderState);

}
