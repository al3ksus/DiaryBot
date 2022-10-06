package com.example.DiaryBot.task;

import com.example.DiaryBot.config.BotConfig;
import com.example.DiaryBot.model.Schedule;
import com.example.DiaryBot.model.enums.BotState;
import com.example.DiaryBot.model.enums.ScheduleState;
import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.ScheduleService;
import com.example.DiaryBot.utils.Constant;
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
@Getter
public class TaskSchedule extends TimerTask {

    private Long scheduleId;

    private Timer timer;

    private Long chatId;

    private ScheduleService scheduleService;

    private ChatService chatService;

    public TaskSchedule(Long scheduleId, Long chatId, Timer timer, ScheduleService scheduleService, ChatService chatService) {
        this.scheduleId = scheduleId;
        this.chatId = chatId;
        this.timer = timer;
        this.scheduleService = scheduleService;
        this.chatService = chatService;
    }

    @Override
    public void run() {
        Optional<Schedule> schedule = scheduleService.getSchedule(scheduleId);

        if (schedule.isPresent()) {

            if (chatService.getChat(chatId).getBotState().equals(BotState.SCHEDULE) &&
                    schedule.get().getScheduleState().equals(ScheduleState.EDITING)) {
                return;
            }

            BotConfig botConfig = new BotConfig();

            HttpURLConnection connection;

            try {
                connection = (HttpURLConnection) new URL(
                        String.format(Constant.SEND_MESSAGE_URL,
                                botConfig.getBotToken(),
                                chatId,
                                "Твое расписание на завтра%0a" + schedule.get().getText().replaceAll("\n", "%0a"))
                ).openConnection();

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
}
