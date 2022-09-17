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
    private final String webhookPath = "https://edf9-2001-67c-418-a001-285c-fe41-f373-2fff.eu.ngrok.io";

    //@Value("${telegram.bot-name}")
    private final String botName = "Dailu_Helper_Bot";

    //@Value("${telegram.bot-token}")
    private final String botToken = "5620340450:AAEStJhaE1cd9x8VJjtyWIcAfcje1LsWjVg";
}
