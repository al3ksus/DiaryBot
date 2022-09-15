package com.example.DiaryBot.service.handler;

import com.example.DiaryBot.model.BotState;
import com.example.DiaryBot.model.Chat;
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

    private final BotStateHandler botStateHandler;

    public CommandHandler(MessageGenerator messageGenerator, ChatService chatService, ReminderService reminderService, KeyBoardService keyBoardService, BotStateHandler botStateHandler) {
        this.messageGenerator = messageGenerator;
        this.chatService = chatService;
        this.reminderService = reminderService;
        this.keyBoardService = keyBoardService;
        this.botStateHandler = botStateHandler;
    }

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
        Optional<Reminder> reminder = reminderService.findWithoutTime();

        if (reminder.isPresent()) {
            SendMessage sendMessage = new SendMessage(
                    String.valueOf(chatId),
                    messageGenerator.unfinishedReminderMessage(reminder.get().getText())
            );

            sendMessage.setReplyMarkup(keyBoardService.getReminderButtonRow());
            return sendMessage;
        }

        chatService.setBotState(chatId, BotState.SET_TEXT_REMINDER);
        return new SendMessage(String.valueOf(chatId), messageGenerator.newReminderMessage());
    }
}
