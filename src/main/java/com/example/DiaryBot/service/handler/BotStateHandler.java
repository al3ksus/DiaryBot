package com.example.DiaryBot.service.handler;

import com.example.DiaryBot.model.enums.BotState;
import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.Schedule;
import com.example.DiaryBot.service.ScheduleService;
import com.example.DiaryBot.task.TaskReminder;
import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.ReminderService;
import com.example.DiaryBot.service.time.TimeParser;
import com.example.DiaryBot.task.TaskSchedule;
import com.example.DiaryBot.telegram.service.MessageGenerator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.Timer;

@Component
public class BotStateHandler {

    private final ReminderService reminderService;

    private final ChatService chatService;

    private final ScheduleService scheduleService;

    private final MessageGenerator messageGenerator;

    private final TimeParser timeParser;

    public BotStateHandler(ReminderService reminderService, ChatService chatService, ScheduleService scheduleService, MessageGenerator messageGenerator, TimeParser timeParser) {
        this.reminderService = reminderService;
        this.chatService = chatService;
        this.scheduleService = scheduleService;
        this.messageGenerator = messageGenerator;
        this.timeParser = timeParser;
    }

    public BotApiMethod<?> handleBotState(Long chatId, String messageText, BotState botState) {

        return switch (botState) {
            case SET_TEXT_REMINDER -> setTextReminder(chatId, messageText);

            case SET_TIME_REMINDER ->  setTimeReminder(chatId, messageText);

            case ADD_SCHEDULE -> addSchedule(chatId, messageText);

            case DELETE_REMINDER -> deleteReminder(chatId, messageText);

            default -> null;
        };
    }

    private BotApiMethod<?> setTextReminder(Long chatId, String messageText) {

        Chat chat = chatService.getChat(chatId);
        chatService.setBotState(chatId, BotState.SET_TIME_REMINDER);
        reminderService.addReminder(chat, messageText);
        return new SendMessage(String.valueOf(chatId), messageGenerator.setTimeMessage());
    }

    private BotApiMethod<?> setTimeReminder(Long chatId, String timeString) {
        Optional<Reminder> reminder = reminderService.findWithoutTime();

        if (reminder.isPresent()) {
            Timer timer = new Timer();
            TaskReminder task = new TaskReminder(reminder.get(), chatId, timer, reminderService);

            try {
                task.getTimer().schedule(task, timeParser.getDelay(timeString));
            }
            catch (ParseException e) {
                return new SendMessage(String.valueOf(chatId), messageGenerator.invalidTimeError());
            }
            catch (IllegalArgumentException e) {
                return new SendMessage(String.valueOf(chatId), messageGenerator.pastTimeError());
            }

            chatService.setBotState(chatId, BotState.DEFAULT);
            reminderService.setTime(reminder.get(), timeString);
            return new SendMessage(String.valueOf(chatId), messageGenerator.reminderSavedMessage());
        }

        return null;
    }

    private BotApiMethod<?> addSchedule(Long chatId, String month) {
        Optional<Schedule> schedule = scheduleService.findWithoutText();

        if (schedule.isPresent()) {
            Timer timer = new Timer();
            TaskSchedule task = new TaskSchedule(month, chatId, timer);
            scheduleService.setText(schedule.get(), month);
            timer.schedule(task, timeParser.getDelayForSchedule(schedule.get().getDayOfWeek()), 604_800_000);
            chatService.setBotState(chatId, BotState.DEFAULT);

            return new SendMessage(
                    String.valueOf(chatId),
                    messageGenerator.scheduleSavedMessage(schedule.get().getDayOfWeek())
            );
        }

        return null;
    }

    private BotApiMethod<?> deleteReminder(Long chatId, String number) {
        try {
            List<Reminder> reminderList = reminderService.findAll(chatService.getChat(chatId));
            reminderService.delete(reminderList.get(Integer.parseInt(number) - 1));
            chatService.setBotState(chatId, BotState.DEFAULT);

            return new SendMessage(
                    String.valueOf(chatId),
                    messageGenerator.reminderDeletedMessage(reminderList.get(Integer.parseInt(number) - 1))
            );
        }
        catch (NumberFormatException | IndexOutOfBoundsException e) {
            return new SendMessage(String.valueOf(chatId), messageGenerator.invalidNumberError());
        }
    }
}
