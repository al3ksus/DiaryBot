package com.example.DiaryBot.service.handler;

import com.example.DiaryBot.model.enums.BotState;
import com.example.DiaryBot.model.Reminder;
import com.example.DiaryBot.model.Schedule;
import com.example.DiaryBot.model.enums.ReminderState;
import com.example.DiaryBot.service.keyboard.KeyboardService;
import com.example.DiaryBot.service.ScheduleService;
import com.example.DiaryBot.task.TaskReminder;
import com.example.DiaryBot.service.ChatService;
import com.example.DiaryBot.service.ReminderService;
import com.example.DiaryBot.service.time.TimeParser;
import com.example.DiaryBot.task.TaskSchedule;
import com.example.DiaryBot.telegram.service.MessageGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.Timer;

@Component
@AllArgsConstructor
public class BotStateHandler {

    private final ReminderService reminderService;

    private final ChatService chatService;

    private final ScheduleService scheduleService;

    private final MessageGenerator messageGenerator;

    private final TimeParser timeParser;

    private final KeyboardService keyboardService;

    public BotApiMethod<?> handleBotState(Long chatId, String messageText, BotState botState) {

        return switch (botState) {
            case SET_TEXT_REMINDER -> setTextReminder(chatId, messageText);

            case SET_TIME_REMINDER ->  setTimeReminder(chatId, messageText);

            case ADD_SCHEDULE -> addSchedule(chatId, messageText);

            case DELETE_REMINDER -> deleteReminder(chatId, messageText);

            case EDIT_REMINDER -> editReminder(chatId, messageText);

            case EDIT_TIME_REMINDER -> editTimeReminder(chatId, messageText);

            case EDIT_TEXT_REMINDER -> editTextReminder(chatId, messageText);

            default -> null;
        };
    }

    private BotApiMethod<?> setTextReminder(Long chatId, String reminderText) {

        chatService.setBotState(chatId, BotState.SET_TIME_REMINDER);
        reminderService.addReminder(chatService.getChat(chatId), reminderText);
        return new SendMessage(String.valueOf(chatId), messageGenerator.setTimeMessage());
    }

    private BotApiMethod<?> setTimeReminder(Long chatId, String timeString) {
        Optional<Reminder> reminder = reminderService.findByState(chatService.getChat(chatId), ReminderState.CREATING);

        if (reminder.isPresent()) {
            Timer timer = new Timer();
            TaskReminder task = new TaskReminder(reminder.get().getId(), chatId, timer, reminderService, chatService, timeParser);

            try {
                task.getTimer().schedule(task, timeParser.getDelay(timeString));
            }
            catch (ParseException e) {
                return new SendMessage(String.valueOf(chatId), messageGenerator.invalidTimeError());
            }
            catch (IllegalArgumentException e) {
                return new SendMessage(String.valueOf(chatId), messageGenerator.pastTimeError());
            }

            reminderService.setTime(reminder.get(), timeString);
            reminderService.setReminderState(reminder.get(), ReminderState.DEFAULT);
            chatService.setBotState(chatId, BotState.DEFAULT);
            return new SendMessage(String.valueOf(chatId), messageGenerator.reminderSavedMessage(reminder.get()));
        }

        return null;
    }

    private BotApiMethod<?> editTimeReminder(Long chatId, String timeString) {
        Optional<Reminder> reminder = reminderService.findByState(chatService.getChat(chatId), ReminderState.EDITING);

        if (reminder.isPresent()) {

            try {
                if (timeParser.isPast(timeString)) {
                    return new SendMessage(String.valueOf(chatId), messageGenerator.pastTimeError());
                }
            } catch (ParseException e) {
                return new SendMessage(String.valueOf(chatId), messageGenerator.invalidTimeError());
            }

            reminderService.setTime(reminder.get(), timeString);
            chatService.setBotState(chatId, BotState.EDIT_REMINDER);
            return saveChanges(chatId);
        }

        return null;
    }

    private BotApiMethod<?> editTextReminder(Long chatId, String text) {
        Optional<Reminder> reminder = reminderService.findByState(chatService.getChat(chatId), ReminderState.EDITING);

        if (reminder.isPresent()) {
            reminderService.setText(reminder.get(), text);
            chatService.setBotState(chatId, BotState.EDIT_REMINDER);
            return saveChanges(chatId);
        }

        return null;
    }

    private BotApiMethod<?> saveChanges(Long chatId) {
        Optional<Reminder> reminder = reminderService.findByState(chatService.getChat(chatId), ReminderState.EDITING);

        if (reminder.isPresent()) {

            Timer timer = new Timer();
            TaskReminder task = new TaskReminder(reminder.get().getId(), chatId, timer, reminderService, chatService, timeParser);

            try {
                task.getTimer().schedule(task, timeParser.getDelay(reminder.get().getTime()));
            }
            catch (ParseException e) {
                return new SendMessage(String.valueOf(chatId), messageGenerator.invalidTimeError());
            }
            catch (IllegalArgumentException e) {
                task.getTimer().schedule(task, 1000);
            }

            chatService.setBotState(chatId, BotState.DEFAULT);
            reminderService.setReminderState(reminder.get(), ReminderState.DEFAULT);
            return new SendMessage(String.valueOf(chatId), messageGenerator.reminderSavedMessage(reminder.get()));
        }

        return null;
    }

    private BotApiMethod<?> editReminder(Long chatId, String number) {
        List<Reminder> reminderList = reminderService.findAll(chatService.getChat(chatId));

        try {
            reminderService.setReminderState(reminderList.get(Integer.parseInt(number) - 1), ReminderState.EDITING);
        }
        catch (NumberFormatException | IndexOutOfBoundsException e) {
            return new SendMessage(String.valueOf(chatId), messageGenerator.invalidNumberError());
        }

        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), messageGenerator.editReminderMessage());
        sendMessage.setReplyMarkup(keyboardService.editReminderButtonRow());
        return sendMessage;
    }

    private BotApiMethod<?> deleteReminder(Long chatId, String number) {
        List<Reminder> reminderList = reminderService.findAll(chatService.getChat(chatId));

        try {
            reminderService.delete(reminderList.get(Integer.parseInt(number) - 1));
        }
        catch (NumberFormatException | IndexOutOfBoundsException e) {
            return new SendMessage(String.valueOf(chatId), messageGenerator.invalidNumberError());
        }

        chatService.setBotState(chatId, BotState.DEFAULT);
        return new SendMessage(
                String.valueOf(chatId),
                messageGenerator.reminderDeletedMessage(reminderList.get(Integer.parseInt(number) - 1))
        );
    }

    private BotApiMethod<?> addSchedule(Long chatId, String month) {
        Optional<Schedule> schedule = scheduleService.findWithoutText();

        if (schedule.isPresent()) {
            Timer timer = new Timer();
            TaskSchedule task = new TaskSchedule(month, chatId, timer);
            scheduleService.setText(schedule.get(), month);
            timer.schedule(task, timeParser.getDelayForSchedule(schedule.get().getDayOfWeek()), 604_800_000);
            chatService.setBotState(chatId, BotState.DEFAULT);

            return new SendMessage(
                    String.valueOf(chatId),
                    messageGenerator.scheduleSavedMessage(schedule.get().getDayOfWeek())
            );
        }

        return null;
    }
}
