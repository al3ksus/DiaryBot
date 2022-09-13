package com.example.DiaryBot.service.time;

import com.example.DiaryBot.telegram.TelegramBot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
@Getter
@Setter
@NoArgsConstructor
public class Task extends TimerTask {

    private TelegramBot telegramBot;

    private Long chatId;

    private String text;

    public Task(Long chatId, String text) {
        this.chatId = chatId;
        this.text = text;
    }

    @Override
    public void run() {
        telegramBot.sendMessage(chatId, text);
    }
}
