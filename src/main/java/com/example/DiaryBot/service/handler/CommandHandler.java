package com.example.DiaryBot.service.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface CommandHandler {
    BotApiMethod<?> handleCommand(Long chatId, String command);
}
