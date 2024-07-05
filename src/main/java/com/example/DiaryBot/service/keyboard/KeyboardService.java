package com.example.DiaryBot.service.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface KeyboardService {
    InlineKeyboardMarkup editReminderButtonRow();
    InlineKeyboardMarkup weekButtonRow();
}
