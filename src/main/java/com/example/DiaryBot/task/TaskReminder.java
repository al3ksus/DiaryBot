package com.example.DiaryBot.task;

import com.example.DiaryBot.config.BotConfig;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.service.ReminderService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaskReminder extends TimerTask {

    private Long reminderId;

    private Long chatId;

    private Timer timer;

    private ReminderService reminderService;


    @Override
    public void run() {

        Optional<Reminder> reminder = reminderService.getReminder(reminderId);

       if (reminder.isPresent()) {
           if (!reminderService.isExist(reminder.get())) {
               timer.cancel();
               return;
           }

           BotConfig botConfig = new BotConfig();

           HttpURLConnection connection;

           try {
               connection = (HttpURLConnection) new URL(
                       String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%d&text=%s",
                               botConfig.getBotToken(), chatId, reminder.get().getText().replaceAll("\n", "%0a"))).openConnection();

               connection.setRequestMethod("GET");
               connection.connect();

               reminderService.delete(reminder.get());
               timer.cancel();

               if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                   return;
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    }
}
