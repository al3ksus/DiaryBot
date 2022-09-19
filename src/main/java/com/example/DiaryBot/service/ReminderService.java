package com.example.DiaryBot.service;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.enums.ReminderState;
import com.example.DiaryBot.repository.ReminderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public void addReminder(Chat chat, String text) {
        reminderRepository.save(new Reminder(chat, text));
    }

    public Optional<Reminder> getReminder(Long id) {
        return reminderRepository.findById(id);
    }


    public void addReminder(Reminder reminder) {
        reminderRepository.save(reminder);
    }

    public Optional<Reminder> findByState(Chat chat, ReminderState reminderState) {
        return reminderRepository.findByChatAndReminderState(chat, reminderState);
    }

    public List<Reminder> findAllByState(Chat chat, ReminderState reminderState) {
        return reminderRepository.findAllByChatAndReminderState(chat, reminderState);
    }

    public void setReminderState(Reminder reminder, ReminderState reminderState) {
        reminder.setReminderState(reminderState);
        reminderRepository.save(reminder);
    }

    public void setTime(Reminder reminder, String time) {
        reminder.setTime(time);
        reminderRepository.save(reminder);
    }

    public void setText(Reminder reminder, String text) {
        reminder.setText(text);
        reminderRepository.save(reminder);
    }

    public void delete(Reminder reminder) {
        reminderRepository.delete(reminder);
    }

    public List<Reminder> findAll(Chat chat) {
        return reminderRepository.findAllByChat(chat);
    }

    public boolean isExist(Reminder reminder) {
        return reminderRepository.existsById(reminder.getId());
    }

    public boolean isExistByState(ReminderState reminderState) {
        return reminderRepository.existsByReminderState(reminderState);
    }

}
