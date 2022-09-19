package com.example.DiaryBot.service.handler;


import com.example.DiaryBot.model.enums.BotState;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.enums.DayOfWeek;
import com.example.DiaryBot.model.enums.ReminderState;
import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.ReminderService;
import com.example.DiaryBot.service.ScheduleService;
import com.example.DiaryBot.service.time.TimeParser;
import com.example.DiaryBot.telegram.service.MessageGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

@Component
@AllArgsConstructor
public class CallbackQueryHandler {

    private final ReminderService reminderService;

    private final ChatService chatService;

    private final MessageGenerator messageGenerator;

    private final ScheduleService scheduleService;

    private final TimeParser timeParser;

    public BotApiMethod<?> handleCallBackQuery(Long chatId, String data) {

        return switch (data) {
            case "ADDREMINDER" -> addReminder(chatId);
            case "SETTIME" -> setTimeReminder(chatId);
            case "SETTEXT" -> setTextReminder(chatId);
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
        if (chatService.getChat(chatId).getBotState().equals(BotState.EDIT_REMINDER)) {
            chatService.setBotState(chatId, BotState.EDIT_TIME_REMINDER);
            return new SendMessage(String.valueOf(chatId), messageGenerator.setTimeMessage());
        }

        return null;
    }

    private BotApiMethod<?> setTextReminder(Long chatId) {
        if (chatService.getChat(chatId).getBotState().equals(BotState.EDIT_REMINDER)) {
            chatService.setBotState(chatId, BotState.EDIT_TEXT_REMINDER);
            return new SendMessage(String.valueOf(chatId), messageGenerator.setTextMessage());
        }

        return null;
    }

    private BotApiMethod<?> addSchedule(Long chatId, DayOfWeek dayOfWeek) {
        scheduleService.addSchedule(dayOfWeek, chatService.getChat(chatId));
        return new SendMessage(String.valueOf(chatId), messageGenerator.setTextScheduleMessage(dayOfWeek));
    }
}