package com.example.DiaryBot.task;

import com.example.DiaryBot.config.BotConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

@Component
@NoArgsConstructor
@Getter
public class TaskSchedule extends TimerTask {

    private String text;

    private Timer timer;

    private Long chatId;

    public TaskSchedule(String text, Long chatId, Timer timer) {
        this.text = text;
        this.chatId = chatId;
        this.timer = timer;
    }

    @Override
    public void run() {

        BotConfig botConfig = new BotConfig();

        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) new URL(
                    String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%d&text=%s",
                            botConfig.getBotToken(), chatId, text.replaceAll("\n", "%0a"))).openConnection();

            connection.setRequestMethod("GET");
            connection.connect();

            timer.cancel();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
