package com.example.DiaryBot.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BotConfig {
    private final String webhookPath = "https://95.214.62.104";

    private final String botName = "Dailu_Helper_Bot";

    private final String botToken = "5620340450:AAEStJhaE1cd9x8VJjtyWIcAfcje1LsWjVg";
}
