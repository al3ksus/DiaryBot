package com.example.DiaryBot.task;

import com.example.DiaryBot.config.BotConfig;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.service.ReminderService;
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
public class TaskReminder extends TimerTask {

    private Reminder reminder;

    private Long chatId;

    private Timer timer;

    private ReminderService reminderService;


    public TaskReminder(Reminder reminder, Long chatId, Timer timer, ReminderService reminderService) {
        this.reminder = reminder;
        this.chatId = chatId;
        this.timer = timer;
        this.reminderService = reminderService;
    }

    @Override
    public void run() {
        BotConfig botConfig = new BotConfig();

        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) new URL(
                    String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%d&text=%s",
                            botConfig.getBotToken(), chatId, reminder.getText().replaceAll("\n", "%0a"))).openConnection();

            connection.setRequestMethod("GET");
            connection.connect();

            reminderService.delete(reminder);
            timer.cancel();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
