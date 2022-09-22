package com.example.DiaryBot.service.time;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
public class TimeFormat {

    public Calendar parseFromString(String time) throws ParseException {
        int hour;
        int minute;
        int day;
        int month;
        int year;
        Calendar calendar = new GregorianCalendar();

        hour = getHour(time);
        time = time.substring(time.indexOf(':') + 1);
        minute = getMinute(time);
        time = time.substring(time.indexOf(' ') + 1);
        day = getDay(time);
        time = time.substring(time.indexOf('.') + 1);
        month = getMonth(time);
        time = time.substring(time.indexOf('.') + 1);
        year = getYear(time);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        System.out.println(calendar.getTime());

        return calendar;
    }

    private int getHour(String time) throws ParseException {
        int hour = -1;

        int int0;
        int int1;
        char char0;
        char char1;

        try {
            char0 = time.charAt(0);
            char1 = time.charAt(1);
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Illegal time format", 20);
        }

        if (Character.isDigit(char0)) {
            int0 = Character.getNumericValue(char0);

            if (Character.isDigit(char1) && time.charAt(2) == ':') {
                int1 = Character.getNumericValue(char1);

                if (int0 <= 1 || (int0 == 2 && int1 <= 3)) {

                    hour = int0 * 10 + int1;
                }
            } else if (char1 == ':') {
                hour = int0;
            }
        }

        System.out.println(hour);
        if (hour == -1) {
            throw new ParseException("Illegal time format", 20);
        } else {
            return hour;
        }
    }

    private int getMinute(String time) throws ParseException {
        int minute = -1;

        int int0;
        int int1;
        char char0;
        char char1;

        try {
            char0 = time.charAt(0);
            char1 = time.charAt(1);
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Illegal time format", 20);
        }

        if (Character.isDigit(char0)) {
            int0 = Character.getNumericValue(char0);
            if (Character.isDigit(char1) && time.charAt(2) == ' ') {
                int1 = Character.getNumericValue(char1);
                if (int0 <= 5) {
                    minute = int0 * 10 + int1;
                }
            } else if (char1 == ' ') {
                minute = int0;
            }
        }
        System.out.println(minute);
        if (minute == -1) {
            throw new ParseException("Illegal time format", 21);
        } else {
            return minute;
        }
    }

    private int getDay(String time) throws ParseException {
        Calendar calendar = Calendar.getInstance();

        int month = calendar.get(Calendar.MONTH);
        int day = -1;
        int int0;
        int int1;
        char char0;
        char char1;

        try {
            char0 = time.charAt(0);
            char1 = time.charAt(1);
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Illegal time format", 20);
        }

        if (Character.isDigit(char0)) {
            int0 = Character.getNumericValue(char0);
            if (Character.isDigit(char1) && time.charAt(2) == '.') {
                int1 = Character.getNumericValue(char1);
                if (int0 <= 2 || (int0 == 3 && int1 <= 1)) {
                    day = int0 * 10 + int1;
                }
            } else if (char1 == '.') {
                day = int0;
            }
        }

        System.out.println(day);
        if (month == 3 || month == 5 || month == 8 || month == 10) {
            if (day > 30) {
                throw new ParseException("Illegal time format", 21);
            }
        } else if (month == 1) {
            if (calendar.get(Calendar.YEAR) % 4 == 0) {
                if (day > 29) {
                    throw new ParseException("Illegal time format", 21);
                }
            } else {
                if (day > 28) {
                    throw new ParseException("Illegal time format", 21);
                }
            }
        } else {
            if (day > 30) {
                throw new ParseException("Illegal time format", 21);
            }
        }

        if (day == -1) {
            throw new ParseException("Illegal time format", 21);
        } else {
            return day;
        }
    }

    private int getMonth(String time) throws ParseException {
        int month = -1;

        int int0;
        int int1;
        char char0;
        char char1;

        try {
            char0 = time.charAt(0);
            char1 = time.charAt(1);
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Illegal time format", 20);
        }

        if (Character.isDigit(char0)) {
            int0 = Character.getNumericValue(char0);
            if (Character.isDigit(char1) && time.charAt(2) == '.') {
                int1 = Character.getNumericValue(char1);
                if (int0 == 0 || (int0 == 1 && int1 <= 2)) {
                    month = int0 * 10 + int1;
                }
            } else if (char1 == '.') {
                month = int0;
            }
        }

        System.out.println(month);
        if (month == -1) {
            throw new ParseException("Illegal time format", 21);
        } else {
            return month;
        }
    }

    private int getYear(String time) throws ParseException {
        int year = -1;

        char char0;
        char char1;
        char char2;
        char char3;

        try {
            char0 = time.charAt(0);
            char1 = time.charAt(1);
            char2 = time.charAt(2);
            char3 = time.charAt(3);
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Illegal time format", 20);
        }

        if (time.length() != 4) {
            throw new ParseException("Illegal time format", 20);
        }

        if (Character.isDigit(char0) && Character.isDigit(char1) && Character.isDigit(char2) && Character.isDigit(char3)) {
            year = Character.getNumericValue(char0) * 1000
                    + Character.getNumericValue(char1) * 100
                    + Character.getNumericValue(char2) * 10
                    + Character.getNumericValue(char3);
        }

        System.out.println(year);
        if (year == -1) {
            throw new ParseException("Illegal time format", 21);
        } else {
            return year;
        }
    }
}