package com.example.DiaryBot.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BotConfig {
    private final String webhookPath = "https://ea40-85-113-138-225.eu.ngrok.io";

    private final String botName = "Dailu_Helper_Bot";

    private final String botToken = "5620340450:AAEStJhaE1cd9x8VJjtyWIcAfcje1LsWjVg";
}
