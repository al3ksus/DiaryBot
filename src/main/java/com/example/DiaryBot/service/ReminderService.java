package com.example.DiaryBot.service;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReminderService {

    @Autowired
    private ReminderRepository reminderRepository;

    public void addReminder(Chat chat, String text) {
        reminderRepository.save(new Reminder(chat, text));
    }

    public Optional<Reminder> findWithoutTime() {
        return reminderRepository.findByTime(null);
    }

    public void setTime(String time) {
        Optional<Reminder> reminder = findWithoutTime();

        if (reminder.isPresent()) {
            reminder.get().setTime(time);
            reminderRepository.save(reminder.get());
        }
    }
}
