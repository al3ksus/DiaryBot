package com.example.DiaryBot.utils;

public class Constant {

    //Time constants
    public static final long DAY = 86_400_000L;

    public static final long WEEK = 604_800_000L;

    //URL addresses
    public static final String SEND_MESSAGE_URL = "https://api.telegram.org/bot%s/sendMessage?chat_id=%d&text=%s";
}
