package com.example.DiaryBot.service.handler;

import com.example.DiaryBot.config.BotConfig;
import com.example.DiaryBot.model.enums.BotState;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.enums.DayOfWeek;
import com.example.DiaryBot.model.enums.ReminderState;
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
            case "ADDREMINDER" -> addReminder(chatId);
            case "SETTIME" -> setTimeReminder(chatId);
            case "SETTEXT" -> setTextReminder(chatId);
            case "STOPEDITING" -> stopEditing(chatId);
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

    private BotApiMethod<?> addReminder(Long chatId) {
        Optional<Reminder> reminder = reminderService.findByState(chatService.getChat(chatId), ReminderState.CREATING);
        reminder.ifPresent(reminderService::delete);
        chatService.setBotState(chatId, BotState.SET_TEXT_REMINDER);
        return new SendMessage(String.valueOf(chatId), messageGenerator.setTextMessage());
    }

    private BotApiMethod<?> setTimeReminder(Long chatId) {
        BotState botState = chatService.getChat(chatId).getBotState();

        if (botState.equals(BotState.SET_TEXT_REMINDER)) {
            chatService.setBotState(chatId, BotState.SET_TIME_REMINDER);
        }
        else if (botState.equals(BotState.EDIT_REMINDER)) {
            chatService.setBotState(chatId, BotState.EDIT_TIME_REMINDER);
        }

        return new SendMessage(String.valueOf(chatId), messageGenerator.setTimeMessage());
    }

    private BotApiMethod<?> setTextReminder(Long chatId) {
        if (chatService.getChat(chatId).getBotState().equals(BotState.EDIT_REMINDER)) {
            chatService.setBotState(chatId, BotState.EDIT_TEXT_REMINDER);
        }

        return new SendMessage(String.valueOf(chatId), messageGenerator.setTextMessage());
    }

    private BotApiMethod<?> stopEditing(Long chatId) {
        Optional<Reminder> reminder = reminderService.findByState(chatService.getChat(chatId), ReminderState.EDITING);

        if (reminder.isPresent()) {
            reminderService.setReminderState(reminder.get(), ReminderState.DEFAULT);
            return new SendMessage(String.valueOf(chatId), messageGenerator.reminderSavedMessage(reminder.get()));
        }

        return null;
    }

    private BotApiMethod<?> addSchedule(Long chatId, DayOfWeek dayOfWeek) {
        scheduleService.addSchedule(dayOfWeek, chatService.getChat(chatId));
        return new SendMessage(String.valueOf(chatId), messageGenerator.setTextScheduleMessage(dayOfWeek));
    }
}