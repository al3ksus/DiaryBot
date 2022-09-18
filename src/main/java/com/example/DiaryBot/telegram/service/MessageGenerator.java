package com.example.DiaryBot.telegram.service;

import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.enums.DayOfWeek;
import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.ReminderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class MessageGenerator {

    private final ReminderService reminderService;

    private final ChatService chatService;

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

    public String setTextScheduleMessage(DayOfWeek dayOfWeek) {
        return "Напиши свое расписание на " + dayOfWeek.getTitle();
    }

    public String scheduleSavedMessage(DayOfWeek dayOfWeek) {
        return "Расписание на " + dayOfWeek.getTitle() + " сохранено";
    }

    public String reminderListMessage(Long chatId, String text) {
        int number = 1;
        List<Reminder> reminderList = reminderService.findAll(chatService.getChat(chatId));

        if(reminderList.isEmpty()) {
            return "Еще нет ни одного напоминания для тебя\n"
                    + "Чтобы добавить, используй /addreminder";
        }

        StringBuilder message = new StringBuilder(text);

        for (Reminder reminder : reminderList) {
            message.append(number)
                    .append(" ")
                    .append(reminder.toString())
                    .append("\n");
            number++;
        }

        return String.valueOf(message);
    }

    public String reminderDeletedMessage(Reminder reminder) {
        return "Напоминание\n" + reminder.toString() + "\nудалено";
    }

    public String invalidNumberError() {
        return "Неверный номер, поробуй еще раз";
    }

    public String invalidTimeError() {
        return "Неверный формат времени, попробуй еще раз";
    }

    public String pastTimeError() {
        return "Нельзя назначить напоминание на время, которое уже прошло, попробуй еще раз";
    }
}
