package com.example.DiaryBot.service.time;

import com.example.DiaryBot.utils.Constant;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
public class TimeParser {

    public long getDelay(String timeString) throws ParseException {
        long delay;
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2;
        TimeFormat timeFormat = new TimeFormat();
        calendar2 = timeFormat.parseFromString(timeString);

        delay = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();

        return delay;
    }

    public long getDelayForSchedule(int dayOfWeekNumber) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = new GregorianCalendar();
        long delay;
        int currentDayOfWeekNumber = calendar1.get(Calendar.DAY_OF_WEEK) - 1;

        if (dayOfWeekNumber < currentDayOfWeekNumber) {
            delay = (7 - (currentDayOfWeekNumber - dayOfWeekNumber)) * Constant.DAY;
        }
        else if (dayOfWeekNumber > currentDayOfWeekNumber) {
            delay = (dayOfWeekNumber - currentDayOfWeekNumber) * Constant.DAY;
        }
        else {
            delay = calendar1.get(Calendar.HOUR_OF_DAY) < 21? 0: Constant.WEEK;
        }

        calendar2.setTimeInMillis(calendar1.getTimeInMillis() + delay - Constant.DAY);
        calendar2.set(Calendar.HOUR_OF_DAY, 21);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        delay = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
        System.out.println(calendar2.getTime());
        System.out.println(delay/1000);
        return delay;
    }

    public boolean isPast(String timeString) throws ParseException {
        TimeFormat timeFormat = new TimeFormat();
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = timeFormat.parseFromString(timeString);

        return calendar2.getTimeInMillis() <= calendar1.getTimeInMillis();
    }
}
