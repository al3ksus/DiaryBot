package com.example.DiaryBot.service.time;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
public class TimeParser {

    public Calendar parseFromString(String timeString) {

        int hour = Integer.parseInt(timeString.substring(0, 2));
        int minute = Integer.parseInt(timeString.substring(3, 5));
        int day = Integer.parseInt(timeString.substring(6, 8));
        int month = Integer.parseInt(timeString.substring(9,11));
        int year = Integer.parseInt(timeString.substring(12));

        Calendar calendar = new GregorianCalendar();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        //System.out.println(calendar.getTime());

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd-MM-yyyy");

        try {
            date = formatter.parse(timeString);
            System.out.println(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return calendar;
    }
}
