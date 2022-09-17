package com.example.DiaryBot.service.handler;

import com.example.DiaryBot.model.enums.BotState;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.enums.DayOfWeek;
import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.ReminderService;
import com.example.DiaryBot.service.ScheduleService;
import com.example.DiaryBot.telegram.service.MessageGenerator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

@Component
public class CallbackQueryHandler {

    private final ReminderService reminderService;

    private final CommandHandler commandHandler;

    private final ChatService chatService;

    private final MessageGenerator messageGenerator;

    private final ScheduleService scheduleService;

    public CallbackQueryHandler(ReminderService reminderService, ChatService chatService, CommandHandler commandHandler, MessageGenerator messageGenerator, ScheduleService scheduleService) {
        this.reminderService = reminderService;
        this.commandHandler = commandHandler;
        this.chatService = chatService;
        this.messageGenerator = messageGenerator;
        this.scheduleService = scheduleService;
    }

    public BotApiMethod<?> handleCallBackQuery(Long chatId, String data) {

        return switch (data) {
            case "ADDREMINDER" -> handleAddReminder(chatId, data);
            case "SETTIME" -> handleSetTime(chatId);
            case "MONDAY" -> addSchedule(chatId, DayOfWeek.MONDAY);
            case "TUESDAY" -> addSchedule(chatId, DayOfWeek.TUESDAY);
            case "WEDNESDAY" -> addSchedule(chatId, DayOfWeek.WEDNESDAY);
            case "THURSDAY" -> addSchedule(chatId, DayOfWeek.THURSDAY);
            case "FRIDAY" -> addSchedule(chatId, DayOfWeek.FRIDAY);
            case "SATURDAY" -> addSchedule(chatId, DayOfWeek.SATURDAY);
            case "SUNDAY" -> addSchedule(chatId, DayOfWeek.SUNDAY);
            default -> null;
        };

    }

    private BotApiMethod<?> handleAddReminder(Long chatId, String data) {
        Optional<Reminder> reminder = reminderService.findWithoutTime();
        reminder.ifPresent(reminderService::delete);
        return commandHandler.handleCommand(chatId, data);
    }

    private BotApiMethod<?> handleSetTime(Long chatId) {
        chatService.setBotState(chatId, BotState.SET_TIME_REMINDER);
        return new SendMessage(String.valueOf(chatId), messageGenerator.setTimeMessage());
    }

    private BotApiMethod<?> addSchedule(Long chatId, DayOfWeek dayOfWeek) {
        scheduleService.addSchedule(dayOfWeek, chatService.getChat(chatId));
        chatService.setBotState(chatId, BotState.ADD_SCHEDULE);
        return new SendMessage(String.valueOf(chatId), messageGenerator.setTextScheduleMessage(dayOfWeek));
    }
}
