package com.example.DiaryBot.service.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface CallbackQueryHandler {
    BotApiMethod<?> handleCallBackQuery(Long chatId, String data);
}
