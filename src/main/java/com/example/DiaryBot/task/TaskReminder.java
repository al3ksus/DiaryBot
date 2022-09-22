package com.example.DiaryBot.task;

import com.example.DiaryBot.config.BotConfig;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.enums.BotState;
import com.example.DiaryBot.model.enums.ReminderState;
import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.ReminderService;
import com.example.DiaryBot.service.time.TimeParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
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

    private ChatService chatService;

    private TimeParser timeParser;

    @Override
    public void run() {
        Optional<Reminder> reminder = reminderService.getReminder(reminderId);
        BotState botState = chatService.getChat(chatId).getBotState();

       if (reminder.isPresent()) {

           try {
               if (!timeParser.isPast(reminder.get().getTime())) {
                   System.out.println("future");
                   return;
               }
           } catch (ParseException e) {
               System.out.println("illegal time");
               throw new RuntimeException(e);
           }

           if (reminder.get().getReminderState().equals(ReminderState.EDITING)){
               if ((botState.equals(BotState.EDIT_REMINDER) ||
                       botState.equals(BotState.EDIT_REMINDER_TIME) ||
                       botState.equals(BotState.EDIT_REMINDER_TEXT))
               ) {
                   System.out.println("editing");
                   timer.cancel();
                   return;
               }
           }

           BotConfig botConfig = new BotConfig();

           HttpURLConnection connection;

           try {
               connection = (HttpURLConnection) new URL(
                       String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%d&text=%s",
                               botConfig.getBotToken(),
                               chatId,
                               "Напоминаю%0a" + reminder.get().getText().replaceAll("\n", "%0a"))
                       ).openConnection();

               connection.setRequestMethod("GET");
               connection.connect();

               reminderService.delete(reminder.get());
               timer.cancel();

               if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                   return;
               }
           } catch (OptimisticLockingFailureException e) {
               return;
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       }
        timer.cancel();
    }
}
