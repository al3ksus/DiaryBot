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

    public String newScheduleMessage() {
        return "Выбери день недели, на который хочешь составить расписание";
    }

    public String setTextScheduleMessage(String dayOfWeek) {
        return "Напиши свое расписание на " + dayOfWeek;
    }

    public String scheduleSavedMessage(String dayOfWeek) {
        return "Расписание на " + dayOfWeek + " сохранено";
    }
}
