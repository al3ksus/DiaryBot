package com.example.DiaryBot.service.handler;

import com.example.DiaryBot.model.enums.BotState;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.KeyBoardService;
import com.example.DiaryBot.service.ReminderService;
import com.example.DiaryBot.telegram.service.MessageGenerator;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

@Component
public class CommandHandler {

    private final MessageGenerator messageGenerator;

    private final ChatService chatService;

    private final ReminderService reminderService;

    private final KeyBoardService keyBoardService;

    public CommandHandler(MessageGenerator messageGenerator, ChatService chatService, ReminderService reminderService, KeyBoardService keyBoardService, BotStateHandler botStateHandler) {
        this.messageGenerator = messageGenerator;
        this.chatService = chatService;
        this.reminderService = reminderService;
        this.keyBoardService = keyBoardService;
    }

    public BotApiMethod<?> handleCommand(Long chatId, String command) {

        return switch (command) {
            case "START" -> start(chatId);
            case "ADDREMINDER" -> addReminder(chatId);
            case "DELETEREMINDER" -> deleteReminder(chatId);
            case "ADDSCHEDULE" -> addSchedule(chatId);
            default -> null;
        };

    }

    private BotApiMethod<?> start(Long chatId) {
        return new SendMessage(String.valueOf(chatId), messageGenerator.startMessage());
    }

    private BotApiMethod<?> addReminder(Long chatId) {
        Optional<Reminder> reminder = reminderService.findWithoutTime();
        chatService.setBotState(chatId, BotState.SET_TEXT_REMINDER);

        if (reminder.isPresent()) {
            SendMessage sendMessage = new SendMessage(
                    String.valueOf(chatId),
                    messageGenerator.unfinishedReminderMessage(reminder.get().getText())
            );

            sendMessage.setReplyMarkup(keyBoardService.getReminderButtonRow());
            return sendMessage;
        }

        return new SendMessage(String.valueOf(chatId), messageGenerator.newReminderMessage());
    }

    private BotApiMethod<?> deleteReminder(Long chatId) {
        chatService.setBotState(chatId, BotState.DELETE_REMINDER);
        return  new SendMessage(String.valueOf(chatId), messageGenerator.deleteReminderMessage(chatId));
    }

    private BotApiMethod<?> addSchedule(Long chatId) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), messageGenerator.newScheduleMessage());
        sendMessage.setReplyMarkup(keyBoardService.getWeekButtonRow());
        chatService.setBotState(chatId, BotState.ADD_SCHEDULE);
        return sendMessage;
    }
}
