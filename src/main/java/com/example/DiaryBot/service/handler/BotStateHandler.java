package com.example.DiaryBot.service.handler;

import com.example.DiaryBot.model.BotState;
import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.ReminderService;
import com.example.DiaryBot.telegram.service.MessageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class BotStateHandler {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageGenerator messageGenerator;

    public BotApiMethod<?> handleBotState(Long chatId, String messageText, BotState botState) {

        switch (botState) {
            case SET_TEXT_REMINDER -> {
                Chat chat = chatService.getChat(chatId);
                chatService.setBotState(chatId, BotState.SET_TIME_REMINDER);
                reminderService.addReminder(chat, messageText);
                return new SendMessage(String.valueOf(chatId), messageGenerator.setTimeMessage());
            }

            case SET_TIME_REMINDER -> {
                chatService.setBotState(chatId, BotState.DEFAULT);
                reminderService.setTime(messageText);
                return new SendMessage(String.valueOf(chatId), messageGenerator.reminderSavedMessage());
            }

            default -> {
                return null;
            }
        }
    }
}
