package com.example.DiaryBot.service.handler;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.enums.BotState;
import com.example.DiaryBot.model.enums.ReminderState;
import com.example.DiaryBot.service.ChatServiceImpl;
import com.example.DiaryBot.service.ReminderServiceImpl;
import com.example.DiaryBot.service.handler.CommandHandlerImpl;
import com.example.DiaryBot.service.keyboard.KeyboardServiceImpl;
import com.example.DiaryBot.telegram.service.MessageGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@SpringBootTest
public class CommandHandlerTest {

    @Autowired
    CommandHandlerImpl commandHandler;
    @Autowired
    ChatServiceImpl chatService;
    @Autowired
    ReminderServiceImpl reminderService;
    @Autowired
    KeyboardServiceImpl keyBoardService;
    @Autowired
    MessageGenerator messageGenerator;

    Long chatId = 1L;

    Chat chat = new Chat(chatId, BotState.DEFAULT);

    @BeforeEach
    void addTestChat() {
        chatService.addChat(chat);
    }

    @AfterEach
    void deleteTestChat() {
        chatService.delete(chatId);
    }

    @Test
    void startCommandTest() {
        Assertions.assertTrue(((SendMessage) commandHandler.handleCommand(chatId, "START")).getText()
                .equals(messageGenerator.startMessage())
                && chatService.getChat(chatId).getBotState() == BotState.DEFAULT
        );
    }

    @Test
    void helpCommandTest() {
        Assertions.assertTrue(((SendMessage) commandHandler.handleCommand(chatId, "HELP")).getText()
                .equals(messageGenerator.help())
                && chatService.getChat(chatId).getBotState() == BotState.DEFAULT
        );
    }

    @Test
    void addReminderCommandTest() {
        reminderService.addReminder(chat, "remind");
        Assertions.assertTrue(((SendMessage) commandHandler.handleCommand(chatId, "ADD")).getText()
                .equals(messageGenerator.setTextMessage())
                && chatService.getChat(chatId).getBotState() == BotState.SET_REMINDER_TEXT
                && reminderService.findByState(chat, ReminderState.CREATING).isEmpty()
        );
    }

    @Test
    void editReminderCommandTest() {
        Reminder reminder = new Reminder(chat, "remind");
        reminder.setReminderState(ReminderState.EDITING);
        reminderService.addReminder(reminder);
        Assertions.assertTrue(((SendMessage) commandHandler.handleCommand(chatId, "EDIT")).getText()
                .equals(messageGenerator.reminderListMessage(chatId, "Введи номер напоминания, которого хочешь редактировать\n\n"))
                && chatService.getChat(chatId).getBotState() == BotState.EDIT_REMINDER
                && reminderService.findByState(chat, ReminderState.EDITING).isEmpty()
        );
        reminder.setReminderState(ReminderState.DEFAULT);
        reminderService.delete(reminder);
    }

    @Test
    void getListCommandTest() {
        Assertions.assertTrue(((SendMessage) commandHandler.handleCommand(chatId, "GETLIST")).getText()
                .equals(messageGenerator.reminderListMessage(chatId, ""))
                && chatService.getChat(chatId).getBotState() == BotState.DEFAULT
        );
    }

    @Test
    void deleteCommandTest() {
        Assertions.assertTrue(((SendMessage) commandHandler.handleCommand(chatId, "DELETE")).getText()
                .equals(messageGenerator.reminderListMessage(chatId, "Введи номер напоминания, которого хочешь удалить\n\n"))
                && chatService.getChat(chatId).getBotState() == BotState.DELETE_REMINDER
        );
    }

    @Test
    void scheduleCommandTest() {
        SendMessage sm = (SendMessage) commandHandler.handleCommand(chatId, "SCHEDULE");
        Assertions.assertTrue(sm.getText()
                .equals(messageGenerator.newScheduleMessage())
                && chatService.getChat(chatId).getBotState() == BotState.SCHEDULE
                && sm.getReplyMarkup().equals(keyBoardService.weekButtonRow())
        );
    }
}
