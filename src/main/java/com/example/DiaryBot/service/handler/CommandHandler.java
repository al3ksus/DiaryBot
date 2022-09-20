package com.example.DiaryBot.service.handler;

import com.example.DiaryBot.model.Schedule;
import com.example.DiaryBot.model.enums.BotState;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.enums.ReminderState;
import com.example.DiaryBot.model.enums.ScheduleState;
import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.ScheduleService;
import com.example.DiaryBot.service.keyboard.KeyboardService;
import com.example.DiaryBot.service.ReminderService;
import com.example.DiaryBot.telegram.service.MessageGenerator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CommandHandler {

    private final MessageGenerator messageGenerator;

    private final ChatService chatService;

    private final ReminderService reminderService;

    private final KeyboardService keyboardService;

    private final ScheduleService scheduleService;

    public BotApiMethod<?> handleCommand(Long chatId, String command) {

        return switch (command) {
            case "START" -> start(chatId);

            case "ADDREMINDER" -> addReminder(chatId);

            case "EDITREMINDER" -> editReminder(chatId);

            case "GETREMINDERLIST" -> getReminderList(chatId);

            case "DELETEREMINDER" -> deleteReminder(chatId);

            case "SCHEDULE" -> schedule(chatId);

            default -> null;
        };

    }

    private BotApiMethod<?> start(Long chatId) {
        chatService.setBotState(chatId, BotState.DEFAULT);
        return new SendMessage(String.valueOf(chatId), messageGenerator.startMessage());
    }

    private BotApiMethod<?> addReminder(Long chatId) {
        Optional<Reminder> reminder = reminderService.findByState(chatService.getChat(chatId), ReminderState.CREATING);
        chatService.setBotState(chatId, BotState.SET_TEXT_REMINDER);
        reminder.ifPresent(reminderService::delete);
        return new SendMessage(String.valueOf(chatId), messageGenerator.setTextMessage());
    }

    private BotApiMethod<?> editReminder(Long chatId) {
        List<Reminder> reminderList = reminderService.findAllByState(chatService.getChat(chatId), ReminderState.EDITING);
        reminderList.forEach(r -> reminderService.setReminderState(r, ReminderState.DEFAULT));
        chatService.setBotState(chatId, BotState.EDIT_REMINDER);
        return  new SendMessage(
                String.valueOf(chatId),
                messageGenerator.reminderListMessage(chatId, "Введи номер напоминания, которого хочешь редактировать\n\n")
        );
    }

    public BotApiMethod<?> getReminderList(Long chatId) {
        return new SendMessage(String.valueOf(chatId), messageGenerator.reminderListMessage(chatId, ""));
    }

    private BotApiMethod<?> deleteReminder(Long chatId) {
        chatService.setBotState(chatId, BotState.DELETE_REMINDER);
        return  new SendMessage(
                String.valueOf(chatId),
                messageGenerator.reminderListMessage(chatId, "Введи номер напоминания, которого хочешь удалить\n\n")
        );
    }

    private BotApiMethod<?> schedule(Long chatId) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), messageGenerator.newScheduleMessage());
        sendMessage.setReplyMarkup(keyboardService.weekButtonRow());
        chatService.setBotState(chatId, BotState.SCHEDULE);
        return sendMessage;
    }
}

