package com.example.DiaryBot.service.time;

import com.example.DiaryBot.model.enums.DayOfWeek;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class TimeParser {

    public long getDelay(String timeString) {
        long delay;
        Date date;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy");

        try {
            date = formatter.parse(timeString);
            delay = date.getTime() - calendar.getTimeInMillis();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return delay;
    }

    public long getDelayForSchedule(DayOfWeek dayOfWeek) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        long delay;
        int currentDayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) + 1;
        //int hour = calendar1.get(Calendar.HOUR);
        delay = dayOfWeek.getNumber() > currentDayOfWeek?
                (dayOfWeek.getNumber() - currentDayOfWeek) * 86_400L:
                (currentDayOfWeek - dayOfWeek.getNumber()) * 86_400L;

        calendar2.setTimeInMillis(calendar1.getTimeInMillis() + delay);
        calendar1 = calendar2;
        calendar1.set(Calendar.HOUR, 13);
        calendar1.set(Calendar.MINUTE, 18);
        calendar2.get(Calendar.HOUR);
        delay = delay + calendar1.getTimeInMillis() - calendar2.getTimeInMillis();

        return delay;
    }
}
