package com.springboot.MyTodoList.controller;

import java.util.HashMap;
import java.util.Map;
import com.springboot.MyTodoList.service.TodoItemBotService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import com.springboot.MyTodoList.util.BotCommands;
import com.springboot.MyTodoList.util.BotHelper;
import com.springboot.MyTodoList.util.BotLabels;
import com.springboot.MyTodoList.util.BotMessages;

public class ToDoItemBotController extends TelegramLongPollingBot {

	private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);

	private String botName;
	private Map<Long, Integer> userUpdatingItemMap = new HashMap<>();
	private Map<Long, Boolean> userLoginStatus = new HashMap<>();

	private TodoItemBotService todoItemBotService;

	public ToDoItemBotController(String botToken, String botName, TodoItemBotService todoItemBotService) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.botName = botName;
		this.todoItemBotService = todoItemBotService;
	}

	private SendMessage removeKeyboard(SendMessage noKeyboardMessage) {
		ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
		noKeyboardMessage.setReplyMarkup(keyboardMarkup);
		return noKeyboardMessage;
	}

	private boolean LoggedIn(Long chatId) {
		return userLoginStatus.getOrDefault(chatId, false);
	}

	private SendMessage messageResponse(String messageTextFromTelegram, Long chatId) {
		SendMessage messageResponse = new SendMessage();

		if (!LoggedIn(chatId) && (messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand())
				|| messageTextFromTelegram.equals(BotLabels.SHOW_MAIN_SCREEN.getLabel()))) {
			return todoItemBotService.startLogin();
		}

		else if (LoggedIn(chatId) && messageTextFromTelegram.equals(BotCommands.LOGOUT_COMMAND.getCommand())) {
			userLoginStatus.put(chatId, false);
			return todoItemBotService.logout(chatId);
		}

		else if (LoggedIn(chatId) && (messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand())
				|| messageTextFromTelegram.equals(BotLabels.SHOW_MAIN_SCREEN.getLabel()))) {
			return todoItemBotService.start();

		}

		else if (LoggedIn(chatId) && messageTextFromTelegram.indexOf(BotLabels.DONE.getLabel()) != -1) {
			return todoItemBotService.done(messageTextFromTelegram);

		}

		else if (LoggedIn(chatId) && messageTextFromTelegram.indexOf(BotLabels.UNDO.getLabel()) != -1) {

			return todoItemBotService.undo(messageTextFromTelegram);

		} else if (LoggedIn(chatId) && messageTextFromTelegram.indexOf(BotLabels.DELETE.getLabel()) != -1) {

			return todoItemBotService.delete(messageTextFromTelegram);

		} else if (LoggedIn(chatId) && messageTextFromTelegram.indexOf(BotLabels.UPDATE_ITEM.getLabel()) != -1) {

			return todoItemBotService.update(messageTextFromTelegram);

		} else if (LoggedIn(chatId) && (messageTextFromTelegram.equals(BotCommands.HIDE_COMMAND.getCommand())
				|| messageTextFromTelegram.equals(BotLabels.HIDE_MAIN_SCREEN.getLabel()))) {

			messageResponse = removeKeyboard(messageResponse);
			messageResponse.setText(BotMessages.BYE.getMessage());
			return messageResponse;

		} else if (LoggedIn(chatId) && (messageTextFromTelegram.equals(BotCommands.TODO_LIST.getCommand())
				|| messageTextFromTelegram.equals(BotLabels.LIST_ALL_ITEMS.getLabel())
				|| messageTextFromTelegram.equals(BotLabels.MY_TODO_LIST.getLabel()))) {

			return todoItemBotService.allItems(messageTextFromTelegram);

		} else if (LoggedIn(chatId) && (messageTextFromTelegram.equals(BotCommands.ADD_ITEM.getCommand())
				|| messageTextFromTelegram.equals(BotLabels.ADD_NEW_ITEM.getLabel()))) {

			return todoItemBotService.addItem(messageTextFromTelegram);

		} else if (LoggedIn(chatId) && (messageTextFromTelegram.equals(BotCommands.USER_LIST.getCommand())
				|| messageTextFromTelegram.equals(BotLabels.LIST_ALL_USERS.getLabel()))) {

			return todoItemBotService.allUsers(messageTextFromTelegram);

		} else if (LoggedIn(chatId) && (messageTextFromTelegram.indexOf("KPI") != -1
				&& messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()) != -1)) {

			return todoItemBotService.seeKpi(messageTextFromTelegram);

		} else if (LoggedIn(chatId) && (messageTextFromTelegram.indexOf("TASKS") != -1
				&& messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()) != -1)) {

			return todoItemBotService.seeUserSummary(messageTextFromTelegram);
		} else if (LoggedIn(chatId)) {

			return todoItemBotService.createItemFromMessage(messageTextFromTelegram);
		} else {
			SendMessage loginResponse = todoItemBotService.loginFromMessage(messageTextFromTelegram, chatId);
			if (loginResponse.getText().equals(BotMessages.LOGIN_SUCCESS.getMessage())) {
				userLoginStatus.put(chatId, true);
			}
			return loginResponse;
		}
	}

	@Override
	public String getBotUsername() {
		return botName;
	};

	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {

			String messageTextFromTelegram = update.getMessage().getText();
			Long chatId = update.getMessage().getChatId();

			if (userUpdatingItemMap.containsKey(chatId)) {
				todoItemBotService.updateItemFromMessage(
						userUpdatingItemMap.get(chatId), messageTextFromTelegram);
			} else {
				SendMessage messageResponse = messageResponse(messageTextFromTelegram, chatId);
				messageResponse.setChatId(String.valueOf(chatId));
				try {
					BotHelper.executeMessage(chatId, messageResponse, this);
				} catch (Exception e) {
					logger.error("Error while sending message: " + e.getMessage());
				}
			}
		}
	}
}