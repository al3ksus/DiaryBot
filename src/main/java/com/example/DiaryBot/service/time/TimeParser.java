package com.example.DiaryBot.service.time;

import com.example.DiaryBot.utils.Constant;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TimeParser {

    public long getDelay(String timeString) throws ParseException {
        long delay;
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2;
        calendar2 = parse(timeString);

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
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        calendar2.setTimeInMillis(format.parse(timeString).getTime());

        return calendar2.getTimeInMillis() <= calendar1.getTimeInMillis();
    }

    private  static Calendar parse(String timeString) throws ParseException {
        int hour;
        int minute;
        int day;
        int month;
        int year;
        String[] timeArray;
        String[] hm;
        String[] dmy;
        Calendar calendar = new GregorianCalendar();
        Pattern pattern = Pattern.compile("^\\d?\\d:\\d?\\d \\d?\\d\\.\\d?\\d\\.\\d?\\d?\\d\\d");
        Matcher matcher = pattern.matcher(timeString);

        if (matcher.matches()) {
            timeArray = timeString.split(" ");
            hm = timeArray[0].split(":");
            dmy = timeArray[1].split("\\.");
            hour = Integer.parseInt(hm[0]);
            minute = Integer.parseInt(hm[1]);
            day = Integer.parseInt(dmy[0]);
            month = Integer.parseInt(dmy[1]);
            year = Integer.parseInt(dmy[2]);

            if (hour > 23 || minute > 59 || day == 0 || day > 31 || month == 0 || month > 12) {
                throw new ParseException("Illegal time format", 1);
            }

            if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day > 30) {
                    throw new ParseException("Illegal time format", 1);
                }
            } else if (month == 2) {
                if (year % 4 == 0) {
                    if (day > 29) {
                        throw new ParseException("Illegal time format", 1);
                    }
                } else {
                    if (day > 28) {
                        throw new ParseException("Illegal time format", 1);
                    }
                }
            }

            if (year < 100) {
                year += 2000;
            }

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            return calendar;
        }
        else {
            throw new ParseException("Illegal time format", 1);
        }
    }
}
