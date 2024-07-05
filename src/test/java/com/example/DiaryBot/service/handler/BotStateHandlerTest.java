package com.example.DiaryBot.service.handler;

import com.example.DiaryBot.model.Chat;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.Schedule;
import com.example.DiaryBot.model.enums.BotState;
import com.example.DiaryBot.model.enums.DayOfWeek;
import com.example.DiaryBot.model.enums.ReminderState;
import com.example.DiaryBot.service.ChatServiceImpl;
import com.example.DiaryBot.service.ReminderServiceImpl;
import com.example.DiaryBot.service.ScheduleServiceImpl;
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
public class BotStateHandlerTest {

    @Autowired
    BotStateHandlerImpl botStateHandler;
    @Autowired
    ChatServiceImpl chatService;
    @Autowired
    ReminderServiceImpl reminderService;
    @Autowired
    ScheduleServiceImpl scheduleService;
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
    void setTextStateTest() {
        Assertions.assertTrue(
                ((SendMessage) botStateHandler.handleBotState(
                        chatId, "text", BotState.SET_REMINDER_TEXT)
                ).getText().equals(messageGenerator.setTimeMessage())
                && chatService.getChat(chatId).getBotState() == BotState.SET_REMINDER_TIME
        );
        reminderService.delete(ReminderState.CREATING);
    }

    @Test
    void setTimeStateTest() {
        reminderService.addReminder(chatService.getChat(chatId), "text");
        Reminder reminder = reminderService.findByState(chat, ReminderState.CREATING).get();
        reminder.setTime("12:00 1.12.2024");
        Assertions.assertTrue(
                ((SendMessage) botStateHandler.handleBotState(
                        chatId, "12:00 1.12.2024", BotState.SET_REMINDER_TIME)
                ).getText().equals(messageGenerator.reminderSavedMessage(reminder))
                        && chatService.getChat(chatId).getBotState() == BotState.DEFAULT
        );
        reminderService.delete(reminder);
    }

    @Test
    void editReminderStateTest() {
        Reminder reminder = new Reminder(chat, "text");
        reminder.setReminderState(ReminderState.DEFAULT);
        reminderService.addReminder(reminder);
        Assertions.assertTrue(
                ((SendMessage) botStateHandler.handleBotState(
                        chatId, "1", BotState.EDIT_REMINDER)
                ).getText().equals(messageGenerator.editReminderMessage())
                        && chatService.getChat(chatId).getBotState() == BotState.DEFAULT
        );

        Assertions.assertTrue(
                ((SendMessage) botStateHandler.handleBotState(
                        chatId, "2", BotState.EDIT_REMINDER)
                ).getText().equals(messageGenerator.invalidNumberError())
                        && chatService.getChat(chatId).getBotState() == BotState.DEFAULT
        );
        reminderService.delete(reminder);
    }

    @Test
    void editReminderTimeStateTest() {
        Reminder reminder = new Reminder(chat, "text");
        reminder.setReminderState(ReminderState.EDITING);
        reminder.setTime("12:00 1.12.2024");
        reminderService.addReminder(reminder);
        Assertions.assertTrue(
                ((SendMessage) botStateHandler.handleBotState(
                        chatId, "12:00 1.12.2024", BotState.EDIT_REMINDER_TIME)
                ).getText().equals(messageGenerator.reminderSavedMessage(reminder))
                        && chatService.getChat(chatId).getBotState() == BotState.DEFAULT
        );

        reminderService.setReminderState(reminder, ReminderState.EDITING);
        Assertions.assertTrue(
                ((SendMessage) botStateHandler.handleBotState(
                        chatId, "12:00 1.1.2024", BotState.EDIT_REMINDER_TIME)
                ).getText().equals(messageGenerator.pastTimeError())
                        && chatService.getChat(chatId).getBotState() == BotState.DEFAULT
        );

        reminderService.setReminderState(reminder, ReminderState.EDITING);
        Assertions.assertTrue(
                ((SendMessage) botStateHandler.handleBotState(
                        chatId, "1.1.2024", BotState.EDIT_REMINDER_TIME)
                ).getText().equals(messageGenerator.invalidTimeError())
                        && chatService.getChat(chatId).getBotState() == BotState.DEFAULT
        );
        reminderService.delete(reminder);
    }

    @Test
    void editReminderTextStateTest() {
        Reminder reminder = new Reminder(chat, "text");
        reminder.setReminderState(ReminderState.EDITING);
        reminderService.addReminder(reminder);
        Assertions.assertTrue(
                ((SendMessage) botStateHandler.handleBotState(
                        chatId, "text", BotState.EDIT_REMINDER_TEXT)
                ).getText().equals(messageGenerator.reminderSavedMessage(reminder))
                        && chatService.getChat(chatId).getBotState() == BotState.DEFAULT
        );
        reminderService.delete(reminder);
    }

    @Test
    void deleteStateTest() {
        Reminder reminder = new Reminder(chat, "text");
        reminder.setReminderState(ReminderState.DEFAULT);
        reminderService.addReminder(reminder);
        Assertions.assertTrue(
                ((SendMessage) botStateHandler.handleBotState(
                        chatId, "1", BotState. DELETE_REMINDER)
                ).getText().equals(messageGenerator.reminderDeletedMessage(reminder))
                        && chatService.getChat(chatId).getBotState() == BotState.DEFAULT
        );
        reminder = new Reminder(chat, "text");
        reminderService.addReminder(reminder);
        Assertions.assertTrue(
                ((SendMessage) botStateHandler.handleBotState(
                        chatId, "2", BotState. DELETE_REMINDER)
                ).getText().equals(messageGenerator.invalidNumberError())
                        && chatService.getChat(chatId).getBotState() == BotState.DEFAULT
        );
        reminderService.delete(reminder);
    }

    @Test
    void scheduleStateTest() {
        Schedule schedule = new Schedule(chat, DayOfWeek.FRIDAY);
        scheduleService.addSchedule(schedule);
        Assertions.assertTrue(
                ((SendMessage) botStateHandler.handleBotState(
                        chatId, "schedule", BotState.SCHEDULE)
                ).getText().equals(messageGenerator.scheduleSavedMessage(DayOfWeek.FRIDAY))
                        && chatService.getChat(chatId).getBotState() == BotState.DEFAULT
        );
        scheduleService.delete(schedule);
    }
}
