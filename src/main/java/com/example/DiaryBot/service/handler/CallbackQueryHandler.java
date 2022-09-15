package com.example.DiaryBot.service.handler;

import com.example.DiaryBot.model.BotState;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.ReminderService;
import com.example.DiaryBot.telegram.service.MessageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
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

    public CallbackQueryHandler(ReminderService reminderService, ChatService chatService, CommandHandler commandHandler, MessageGenerator messageGenerator) {
        this.reminderService = reminderService;
        this.commandHandler = commandHandler;
        this.chatService = chatService;
        this.messageGenerator = messageGenerator;
    }

    public BotApiMethod<?> handleCallBackQuery(Long chatId, String data) {

        return switch (data) {
            case "ADDREMINDER" -> handleAddReminder(chatId, data);
            case "SETTIME" -> handleSetTime(chatId);
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
}
