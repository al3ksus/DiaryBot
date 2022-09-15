package com.example.DiaryBot.telegram.service;

import org.springframework.stereotype.Component;

@Component
public class MessageGenerator {

    public String startMessage() {
        return "Привет!";
    }

    public String newReminderMessage() {
        return "Ты хочешь создать новое напоминание.\n"
                + "Введи текст напоминания";

    }

    public String setTimeMessage() {
        return "Отлично, теперь введи время напоминания";
    }

    public String reminderSavedMessage() {
        return "Напоминание сохранено";
    }

    public String unfinishedReminderMessage(String text) {
        return "Уже есть напоминание\n \""
                + text
                + "\", \nкоторому вы не указали время";
    }
}
