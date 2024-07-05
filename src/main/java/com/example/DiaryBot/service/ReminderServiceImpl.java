package com.example.DiaryBot.service;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.enums.ReminderState;
import com.example.DiaryBot.repository.ReminderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReminderServiceImpl implements ReminderService{

    private final ReminderRepository reminderRepository;

    public ReminderServiceImpl(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    @Override
    public void addReminder(Chat chat, String text) {
        reminderRepository.save(new Reminder(chat, text));
    }

    @Override
    public Optional<Reminder> getReminder(Long id) {
        return reminderRepository.findById(id);
    }

    @Override
    public Optional<Reminder> findByState(Chat chat, ReminderState reminderState) {
        return reminderRepository.findByChatAndReminderState(chat, reminderState);
    }

    @Override
    public List<Reminder> findAllByState(Chat chat, ReminderState reminderState) {
        return reminderRepository.findAllByChatAndReminderState(chat, reminderState);
    }

    @Override
    public void setReminderState(Reminder reminder, ReminderState reminderState) {
        reminder.setReminderState(reminderState);
        reminderRepository.save(reminder);
    }

    @Override
    public void setTime(Reminder reminder, String time) {
        reminder.setTime(time);
        reminderRepository.save(reminder);
    }

    @Override
    public void setText(Reminder reminder, String text) {
        reminder.setText(text);
        reminderRepository.save(reminder);
    }

    @Override
    public void delete(Reminder reminder) {
        reminderRepository.delete(reminder);
    }

    @Override
    public List<Reminder> findAll(Chat chat) {
        return reminderRepository.findAllByChat(chat);
    }

}
