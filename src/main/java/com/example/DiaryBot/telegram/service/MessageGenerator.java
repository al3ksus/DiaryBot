package com.example.DiaryBot.telegram.service;

import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.enums.DayOfWeek;
import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.ReminderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
@AllArgsConstructor
public class MessageGenerator {

    private final ReminderService reminderService;

    private final ChatService chatService;

    public String startMessage() {
        return "Привет!\nДля более подробной информации, используй команду /help";
    }

    public String help() {
        return """
                Этот бот может отправлять тебе напоминаия в указанное тобой
                время. Также ты можешь составить расписание на каждый
                день недели, а бот отправит его тебе в 21:00 перед указанным
                днем, то есть расписание на вторник бот отправит в
                понедельник в 21:00, чтобы ты с вечера мог подготовиться
                к следующему дню.
                
                Команды:
                /start    инициализация чата, это можно сделать один раз в начале использования
                /help    получить подробную информацию о боте
                /add    добавить напоминание
                /getlist    посмотреть список всех своих напоминаний
                /edit    редактировать напоминание
                /delete    удалить напоминание
                /schedule    составить расписание
                
                Обратите внимание на формат ввода времени при добавлении
                напоминания, он должен быть таким:
                часы:минуты ден.месяц.год. Время и дата разделены
                проблелом, часы и минуты двоеточием,
                а день, месяц и год точкой, при это год состоит из 4 цифр.
                
                для вопросов и предложений пишите на почту
                aleksejukrainskij7554@gmail.com""";
    }

    public String setTextMessage() {
        return "Введи текст напоминания";

    }

    public String setTimeMessage() {
        Calendar calendar = Calendar.getInstance();
        String time = calendar.get(Calendar.HOUR_OF_DAY)
                + ":" + calendar.get(Calendar.MINUTE)
                + " " + calendar.get(Calendar.DAY_OF_MONTH)
                + "." + (calendar.get(Calendar.MONTH) + 1)
                + "." + calendar.get(Calendar.YEAR);

        return "Введи время напоминания\n"
                + "Пример ввода: " + time;
    }

    public String reminderSavedMessage(Reminder reminder) {

        return "Напоминание\n"
                + reminder.toString()
                + "\nсохранено" ;
    }

    public String editReminderMessage() {
        return "Выбери, что хочешь изменить";
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
                    + "Чтобы добавить, используй /add";
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
