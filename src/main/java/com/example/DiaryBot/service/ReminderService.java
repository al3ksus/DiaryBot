package com.example.DiaryBot.service;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.enums.ReminderState;

import java.util.List;
import java.util.Optional;

public interface ReminderService {
    void addReminder(Chat chat, String text);
    Optional<Reminder> getReminder(Long id);
    Optional<Reminder> findByState(Chat chat, ReminderState reminderState);
    List<Reminder> findAllByState(Chat chat, ReminderState reminderState);
    void setReminderState(Reminder reminder, ReminderState reminderState);
    void setTime(Reminder reminder, String time);
    void setText(Reminder reminder, String text);
    void delete(Reminder reminder);
    List<Reminder> findAll(Chat chat);
}
