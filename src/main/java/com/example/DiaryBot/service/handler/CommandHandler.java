package com.example.DiaryBot.service.handler;

import com.example.DiaryBot.model.BotState;
import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.telegram.service.MessageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class CommandHandler {

    @Autowired
    private MessageGenerator messageGenerator;

    @Autowired
    private ChatService chatService;

    public BotApiMethod<?> handleCommand(Long chatId, String command) {

        return switch (command) {
            case "START" -> handleStart(chatId);
            case "ADDREMINDER" -> handleAddReminder(chatId);
            default -> null;
        };
    }

    private BotApiMethod<?> handleStart(Long chatId) {
        return new SendMessage(String.valueOf(chatId), messageGenerator.startMessage());
    }

    private BotApiMethod<?> handleAddReminder(Long chatId) {
        chatService.setBotState(chatId, BotState.SET_TEXT_REMINDER);

        return new SendMessage(String.valueOf(chatId), messageGenerator.newReminderMessage());
    }
}
