package com.example.DiaryBot.service.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardServiceImpl implements KeyboardService {

    @Override
    public InlineKeyboardMarkup editReminderButtonRow() {

        InlineKeyboardButton buttonText = new InlineKeyboardButton();
        buttonText.setText("Текст");
        buttonText.setCallbackData("EditText");

        InlineKeyboardButton buttonTime = new InlineKeyboardButton();
        buttonTime.setText("Время");
        buttonTime.setCallbackData("EditTime");

        return getInlineKeyboardMarkup(buttonText, buttonTime);
    }

    @Override
    public InlineKeyboardMarkup weekButtonRow() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonMonday = new InlineKeyboardButton();
        buttonMonday.setText("Понедельник");
        buttonMonday.setCallbackData("Monday");

        InlineKeyboardButton buttonTuesday = new InlineKeyboardButton();
        buttonTuesday.setText("Вторник");
        buttonTuesday.setCallbackData("Tuesday");

        InlineKeyboardButton buttonWednesday = new InlineKeyboardButton();
        buttonWednesday.setText("Среда");
        buttonWednesday.setCallbackData("Wednesday");

        InlineKeyboardButton buttonThursday = new InlineKeyboardButton();
        buttonThursday.setText("Четверг");
        buttonThursday.setCallbackData("Thursday");

        InlineKeyboardButton buttonFriday = new InlineKeyboardButton();
        buttonFriday.setText("Пятница");
        buttonFriday.setCallbackData("Friday");

        InlineKeyboardButton buttonSaturday = new InlineKeyboardButton();
        buttonSaturday.setText("Суббота");
        buttonSaturday.setCallbackData("Saturday");

        InlineKeyboardButton buttonSunday = new InlineKeyboardButton();
        buttonSunday.setText("Воскресенье");
        buttonSunday.setCallbackData("Sunday");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(buttonMonday);
        row1.add(buttonTuesday);
        row1.add(buttonWednesday);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(buttonThursday);
        row2.add(buttonFriday);
        row2.add(buttonSaturday);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(buttonSunday);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);
        rowList.add(row3);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(InlineKeyboardButton button1, InlineKeyboardButton button2) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button1);
        row.add(button2);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
