package com.example.DiaryBot.telegram.service;

import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.handler.BotStateHandler;
import com.example.DiaryBot.service.handler.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class BotService {

    @Autowired
    private CommandHandler commandHandler;

    @Autowired
    private BotStateHandler botStateHandler;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageGenerator messageGenerator;

    public BotApiMethod<?> handleUpdate(Update update) {
        Long chatId = 0L;
        String messageText = "";

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();

            if (update.getMessage().hasText()) {
                messageText = update.getMessage().getText();
            }
        }

        return handleMessage(chatId, messageText);
    }

    private BotApiMethod<?> handleMessage(Long chatId, String messageText) {

        if (!chatService.isExist(chatId)) {
            chatService.addChat(chatId);
            return new SendMessage(String.valueOf(chatId), messageGenerator.startMessage());
        }

        if (messageText.charAt(0) == '/') {
            return commandHandler.handleCommand(chatId, messageText.substring(1).toUpperCase());
        }
        else {
            return botStateHandler.handleBotState(chatId, messageText, chatService.getChat(chatId).getBotState());
        }
    }
}
