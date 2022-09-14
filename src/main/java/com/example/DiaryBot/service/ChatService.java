package com.example.DiaryBot.service;

import com.example.DiaryBot.model.BotState;
import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public void addChat(Long chatId) {
        chatRepository.save(new Chat(chatId, BotState.DEFAULT));
    }

    public Chat getChat(Long chatId) {
        return chatRepository.getByChatId(chatId);
    }

    public void setBotState(Long chatId, BotState botState){
        Chat chat = chatRepository.getByChatId(chatId);
        chat.setBotState(botState);
        chatRepository.save(chat);
    }

    public boolean isExist(Long chatId) {
        return chatRepository.existsByChatId(chatId);
    }
}
