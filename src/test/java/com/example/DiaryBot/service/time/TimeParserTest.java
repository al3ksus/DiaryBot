package com.example.DiaryBot.service.time;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
public class TimeParserTest {

    static TimeParser timeParser;
    static String time;

    @BeforeAll
    static void init() {
        timeParser = new TimeParser();
        time = "12:00 1.7.2024";
    }

    @SneakyThrows
    @Test
    void isPastTest() {
        Assertions.assertTrue(timeParser.isPast(time));
    }

    @SneakyThrows
    @Test
    void parseExceptionTest() {
        Assertions.assertThrows(ParseException.class, () -> timeParser.getDelay("qwerty"));
    }
}
