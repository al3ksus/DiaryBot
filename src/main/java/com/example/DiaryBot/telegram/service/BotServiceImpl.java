package com.example.DiaryBot.telegram.service;

import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.handler.BotStateHandler;

import com.example.DiaryBot.service.handler.CallbackQueryHandler;
import com.example.DiaryBot.service.handler.CommandHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@AllArgsConstructor
public class BotServiceImpl implements BotService {

    private final CommandHandler commandHandler;

    private final BotStateHandler botStateHandler;

    private final ChatService chatService;

    private final MessageGenerator messageGenerator;

    private final CallbackQueryHandler callbackQueryHandler;

    @Override
    public BotApiMethod<?> handleUpdate(Update update) {
        Long chatId = 0L;
        String messageText = "";

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();

            if (update.getMessage().hasText()) {
                messageText = update.getMessage().getText();
                return handleMessage(chatId, messageText);
            }
        }
        else if (update.hasCallbackQuery()){
            chatId = update.getCallbackQuery().getMessage().getChatId();
            messageText = update.getCallbackQuery().getData().toUpperCase();

            return callbackQueryHandler.handleCallBackQuery(chatId, messageText);
        }

        return null;
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
