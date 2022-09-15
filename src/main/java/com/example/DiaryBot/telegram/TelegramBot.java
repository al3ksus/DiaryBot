package com.example.DiaryBot.telegram;

import com.example.DiaryBot.telegram.service.BotService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Component
@Setter
public class TelegramBot extends SpringWebhookBot {
    private String botPath;
    private String botUsername;
    private String botToken;

    @Autowired
    private BotService botService;

    public TelegramBot(SetWebhook setWebhook) {
        super(setWebhook);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        System.out.println();
        return botService.handleUpdate(update);
    }
}
