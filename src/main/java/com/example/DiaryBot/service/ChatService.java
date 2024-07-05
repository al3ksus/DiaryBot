package com.example.DiaryBot.service;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.enums.BotState;

public interface ChatService {
    void addChat(Long chatId);
    Chat getChat(Long chatId);
    void setBotState(Long chatId, BotState botState);
    boolean isExist(Long chatId);
}
