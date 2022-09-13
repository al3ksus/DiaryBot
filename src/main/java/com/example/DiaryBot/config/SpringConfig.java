package com.example.DiaryBot.config;

import com.example.DiaryBot.telegram.TelegramBot;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
public class SpringConfig {

    private final BotConfig botConfig;

    public SpringConfig(BotConfig botConfig) {
        this.botConfig = botConfig;
    }
    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botConfig.getWebhookPath()).build();
    }

    @Bean
    public TelegramBot springWebhookBot(SetWebhook setWebhook) {

        TelegramBot telegramBot = new TelegramBot(setWebhook);

        telegramBot.setBotPath(botConfig.getWebhookPath());
        telegramBot.setBotToken(botConfig.getBotToken());
        telegramBot.setBotUsername(botConfig.getBotName());

        return telegramBot;
    }
}
