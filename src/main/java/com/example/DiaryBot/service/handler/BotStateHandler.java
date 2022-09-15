package com.example.DiaryBot.service.handler;

import com.example.DiaryBot.model.BotState;
import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.Task;
import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.ReminderService;
import com.example.DiaryBot.service.time.TimeParser;
import com.example.DiaryBot.telegram.service.MessageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;
import java.util.Timer;

@Component
public class BotStateHandler {

    private final ReminderService reminderService;

    private final ChatService chatService;

    private final MessageGenerator messageGenerator;

    private final TimeParser timeParser;

    public BotStateHandler(ReminderService reminderService, ChatService chatService, MessageGenerator messageGenerator, TimeParser timeParser) {
        this.reminderService = reminderService;
        this.chatService = chatService;
        this.messageGenerator = messageGenerator;
        this.timeParser = timeParser;
    }

    public BotApiMethod<?> handleBotState(Long chatId, String messageText, BotState botState) {

        switch (botState) {
            case SET_TEXT_REMINDER -> {
                return handleSetTextReminder(chatId, messageText);
            }

            case SET_TIME_REMINDER -> {
                return handleSetTimeReminder(chatId, messageText);
            }
        }

        return null;
    }

    private BotApiMethod<?> handleSetTextReminder(Long chatId, String messageText) {

        Chat chat = chatService.getChat(chatId);
        chatService.setBotState(chatId, BotState.SET_TIME_REMINDER);
        reminderService.addReminder(chat, messageText);
        return new SendMessage(String.valueOf(chatId), messageGenerator.setTimeMessage());
    }

    private BotApiMethod<?> handleSetTimeReminder(Long chatId, String messageText) {
        chatService.setBotState(chatId, BotState.DEFAULT);
        Optional<Reminder> reminder = reminderService.findWithoutTime();

        if (reminder.isPresent()) {
            Timer timer = new Timer();
            Task task = new Task(reminder.get().getText(), chatId);
            reminderService.setTime(reminder.get(), messageText);
            timer.schedule(task, timeParser.parseFromString(messageText));
            return new SendMessage(String.valueOf(chatId), messageGenerator.reminderSavedMessage());
        }

        return null;
    }
}
