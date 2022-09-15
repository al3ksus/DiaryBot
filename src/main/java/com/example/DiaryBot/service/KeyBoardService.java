package com.example.DiaryBot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyBoardService {

    public InlineKeyboardMarkup getReminderButtonRow() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonContinue = new InlineKeyboardButton();
        buttonContinue.setText("Указать время");
        buttonContinue.setCallbackData("SetTime");

        InlineKeyboardButton buttonNew = new InlineKeyboardButton();
        buttonNew.setText("Добавить новое");
        buttonNew.setCallbackData("AddReminder");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(buttonContinue);
        row.add(buttonNew);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
