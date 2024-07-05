package com.example.DiaryBot.service;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.enums.BotState;

public interface ChatService {
    void addChat(Long chatId);
    void addChat(Chat chat);
    Chat getChat(Long chatId);
    void setBotState(Long chatId, BotState botState);
    boolean isExist(Long chatId);
    void delete(Long chatId);
}
