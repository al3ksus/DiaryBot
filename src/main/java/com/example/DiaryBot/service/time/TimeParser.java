package com.example.DiaryBot.service.time;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class TimeParser {

    public long parseFromString(String timeString) {

        Date date;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        Date currentDate = calendar.getTime();
        long delay;

        try {
            date = formatter.parse(timeString);
            delay = date.getTime() - currentDate.getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return delay;
    }
}
