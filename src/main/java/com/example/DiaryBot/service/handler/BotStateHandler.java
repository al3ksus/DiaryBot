package com.example.DiaryBot.service.handler;

import com.example.DiaryBot.model.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface BotStateHandler {
    BotApiMethod<?> handleBotState(Long chatId, String messageText, BotState botState);
}
