package com.example.DiaryBot.telegram.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotService {
    BotApiMethod<?> handleUpdate(Update update);
}
