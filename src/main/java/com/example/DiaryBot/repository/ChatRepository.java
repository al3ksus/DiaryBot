package com.example.DiaryBot.repository;

import com.example.DiaryBot.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat getByChatId(Long chatId);

    boolean existsByChatId(Long chatId);
}
