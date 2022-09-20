package com.example.DiaryBot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
//@PropertySource("main\\resources\\application.yaml")
@Getter
public class BotConfig {
    //@Value("${telegram.webhook-path}")
    private final String webhookPath = "https://8680-85-113-138-225.eu.ngrok.io";

    //@Value("${telegram.bot-name}")
    private final String botName = "Dailu_Helper_Bot";

    //@Value("${telegram.bot-token}")
    private final String botToken = "5620340450:AAEStJhaE1cd9x8VJjtyWIcAfcje1LsWjVg";
}
