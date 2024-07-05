package com.example.DiaryBot.service;

import com.example.DiaryBot.model.enums.BotState;
import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.repository.ChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void addChat(Long chatId) {
        chatRepository.save(new Chat(chatId, BotState.DEFAULT));
    }

    @Override
    public void addChat(Chat chat) {
        chatRepository.save(chat);
    }

    @Override
    public Chat getChat(Long chatId) {
        return chatRepository.getByChatId(chatId);
    }

    @Override
    public void setBotState(Long chatId, BotState botState){
        Chat chat = chatRepository.getByChatId(chatId);
        chat.setBotState(botState);
        chatRepository.save(chat);
    }

    @Override
    public boolean isExist(Long chatId) {
        return chatRepository.existsByChatId(chatId);
    }

    @Override
    @Transactional
    public void delete(Long chatId) {
        chatRepository.deleteByChatId(chatId);
    }
}
