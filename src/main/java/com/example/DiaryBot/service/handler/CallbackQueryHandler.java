package com.example.DiaryBot.service.handler;


import com.example.DiaryBot.model.Schedule;
import com.example.DiaryBot.model.enums.BotState;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.enums.DayOfWeek;
import com.example.DiaryBot.model.enums.ReminderState;
import com.example.DiaryBot.model.enums.ScheduleState;
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
            case "EDITTIME" -> editTimeReminder(chatId);
            case "EDITTEXT" -> editTextReminder(chatId);
            case "MONDAY" -> schedule(chatId, DayOfWeek.MONDAY);
            case "TUESDAY" -> schedule(chatId, DayOfWeek.TUESDAY);
            case "WEDNESDAY" -> schedule(chatId, DayOfWeek.WEDNESDAY);
            case "THURSDAY" -> schedule(chatId, DayOfWeek.THURSDAY);
            case "FRIDAY" -> schedule(chatId, DayOfWeek.FRIDAY);
            case "SATURDAY" -> schedule(chatId, DayOfWeek.SATURDAY);
            case "SUNDAY" -> schedule(chatId, DayOfWeek.SUNDAY);
            default -> null;
        };

    }

    private BotApiMethod<?> editTimeReminder(Long chatId) {
        if (chatService.getChat(chatId).getBotState().equals(BotState.EDIT_REMINDER)) {
            chatService.setBotState(chatId, BotState.EDIT_TIME_REMINDER);
            return new SendMessage(String.valueOf(chatId), messageGenerator.setTimeMessage());
        }

        return null;
    }

    private BotApiMethod<?> editTextReminder(Long chatId) {
        if (chatService.getChat(chatId).getBotState().equals(BotState.EDIT_REMINDER)) {
            chatService.setBotState(chatId, BotState.EDIT_TEXT_REMINDER);
            return new SendMessage(String.valueOf(chatId), messageGenerator.setTextMessage());
        }

        return null;
    }

    private BotApiMethod<?> schedule(Long chatId, DayOfWeek dayOfWeek) {
        Optional<Schedule> scheduleByState = scheduleService.findByState(chatService.getChat(chatId), ScheduleState.EDITING);
        Optional<Schedule> scheduleByDay = scheduleService.findByDay(chatService.getChat(chatId), dayOfWeek);

        if (scheduleByDay.isEmpty()) {
            scheduleService.addSchedule(chatService.getChat(chatId), dayOfWeek);
        }

        scheduleByState.ifPresent(s -> scheduleService.setState(s, ScheduleState.DEFAULT));
        scheduleByDay.ifPresent(s -> scheduleService.setState(s, ScheduleState.EDITING));
        chatService.setBotState(chatId, BotState.SCHEDULE);
        return new SendMessage(String.valueOf(chatId), messageGenerator.setTextScheduleMessage(dayOfWeek));
    }
}