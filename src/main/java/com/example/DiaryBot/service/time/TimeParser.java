package com.example.DiaryBot.service.time;

import com.example.DiaryBot.model.enums.DayOfWeek;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class TimeParser {

    public long getDelay(String timeString) throws ParseException {
        long delay;
        Date date;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy");

        date = formatter.parse(timeString);
        delay = date.getTime() - calendar.getTimeInMillis();

        System.out.println(delay);
        return delay;
    }

    public long getDelayForSchedule(DayOfWeek dayOfWeek) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        long delay;
        int currentDayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;

        if (dayOfWeek.getNumber() < currentDayOfWeek) {
            delay = (7 - (currentDayOfWeek - dayOfWeek.getNumber())) * 86_400_000L;
        }
        else if (dayOfWeek.getNumber() > currentDayOfWeek) {
            delay = (dayOfWeek.getNumber() - currentDayOfWeek) * 86_400_000L;
        }
        else {
            delay = calendar1.get(Calendar.HOUR_OF_DAY) < 21? 0: 7 * 86_400_000L;
        }

        calendar2.setTimeInMillis(calendar1.getTimeInMillis() + delay);
        calendar1.setTimeInMillis(calendar2.getTimeInMillis());
        calendar1.set(Calendar.HOUR_OF_DAY, 21);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        delay = delay + calendar1.getTimeInMillis() - calendar2.getTimeInMillis();

        return delay;
    }

    public boolean isPast(String timeString) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy");

        return formatter.parse(timeString).getTime() < calendar.getTimeInMillis();
    }
}
