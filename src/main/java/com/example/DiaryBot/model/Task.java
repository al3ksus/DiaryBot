package com.example.DiaryBot.model;

import com.example.DiaryBot.config.BotConfig;
import com.example.DiaryBot.config.SpringConfig;
import com.example.DiaryBot.telegram.TelegramBot;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TimerTask;

@Component
@NoArgsConstructor
public class Task extends TimerTask {

    private String text;

    private Long chatId;

    public Task(String text, Long chatId) {
        this.text = text;
        this.chatId = chatId;
    }

    @Override
    public void run() {
        BotConfig botConfig = new BotConfig();

        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) new URL(
                    String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%d&text=%s",
                            botConfig.getBotToken(), chatId, text)).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return;
            }

        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }
}
