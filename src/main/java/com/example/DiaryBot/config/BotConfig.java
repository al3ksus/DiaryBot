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
    private final String webhookPath = "https://a324-62-76-212-71.eu.ngrok.io";

    //@Value("${telegram.bot-name}")
    private final String botName = "Dailu_Helper_Bot";

    //@Value("${telegram.bot-token}")
    private final String botToken = "5620340450:AAEStJhaE1cd9x8VJjtyWIcAfcje1LsWjVg";
}
