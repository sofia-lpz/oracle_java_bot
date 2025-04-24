package com.springboot.MyTodoList.controller;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.State;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.model.Kpi;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.service.StateService;
import com.springboot.MyTodoList.service.UserService;
import com.springboot.MyTodoList.service.KpiService;
import com.springboot.MyTodoList.service.ProjectService;
import com.springboot.MyTodoList.service.SprintService;
import com.springboot.MyTodoList.service.TeamService;
import com.springboot.MyTodoList.service.ToDoItemService;

import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.util.BotCommands;
import com.springboot.MyTodoList.util.BotHelper;
import com.springboot.MyTodoList.util.BotLabels;
import com.springboot.MyTodoList.util.BotMessages;

public class ToDoItemBotController extends TelegramLongPollingBot {

	private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);
	private ToDoItemService toDoItemService;
	private StateService stateService;
	private UserService userService;
	private ProjectService projectService;
	private SprintService sprintService;
	private KpiService kpiService;
	private TeamService teamService;
	private String botName;
	private Map<Long, Integer> userUpdatingItemMap = new HashMap<>();

	// Constructor
	public ToDoItemBotController(String botToken, String botName, ToDoItemService toDoItemService,
			StateService stateService, UserService userService, ProjectService projectService,
			SprintService sprintService,
			KpiService kpiService, TeamService teamService) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.toDoItemService = toDoItemService;
		this.botName = botName;
		this.stateService = stateService;
		this.userService = userService;
		this.projectService = projectService;
		this.sprintService = sprintService;
		this.kpiService = kpiService;
		this.teamService = teamService;
	}

	@Override
	public String getBotUsername() {
		return botName;
	}

	private void formatKpiProgress(StringBuilder sb, Map<String, Integer> kpiData) {
		int sum = kpiData.get("sum");
		int total = kpiData.get("total");
		int percent = total > 0 ? (sum * 100) / total : 0;

		sb.append(String.format("%d", percent));
	}

	private String formatTodoList(List<ToDoItem> items) {
		logger.debug("formatTodoList() called with {} items", items.size());

		if (items.isEmpty()) {
			return "No tasks found.";
		}

		StringBuilder sb = new StringBuilder("Your Todo List:\n\n");

		for (ToDoItem item : items) {
			sb.append(String.format("📌 %s\n", item.getTitle()))
					.append(String.format("Description: %s\n", item.getDescription()))
					.append(String.format("Status: %s\n", item.isDone() ? "✅ Done" : "⏳ Pending"))
					.append("\n");
		}

		logger.debug("formatTodoList() returning: {}", sb.toString());

		return sb.toString();
	}

	private void start(Long chatId) {
		SendMessage messageToTelegram = new SendMessage();
		messageToTelegram.setChatId(chatId);
		messageToTelegram.setText(BotMessages.HELLO_MYTODO_BOT.getMessage());

		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboard = new ArrayList<>();

		// first keyboard
		KeyboardRow row = new KeyboardRow();
		row.add(BotLabels.LIST_ALL_ITEMS.getLabel());
		row.add(BotLabels.ADD_NEW_ITEM.getLabel());
		row.add(BotLabels.LIST_ALL_USERS.getLabel());
		keyboard.add(row);

		row = new KeyboardRow();
		row.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
		row.add(BotLabels.HIDE_MAIN_SCREEN.getLabel());
		keyboard.add(row);

		// Set the keyboard
		keyboardMarkup.setKeyboard(keyboard);

		// Add the keyboard markup
		messageToTelegram.setReplyMarkup(keyboardMarkup);

		try {
			execute(messageToTelegram);
		} catch (TelegramApiException e) {
			logger.error(e.getLocalizedMessage(), e);
			messageToTelegram.setText(BotMessages.ERROR.getMessage());
		}
	}

	private void done(String messageTextFromTelegram, Long chatId) {
		String done = messageTextFromTelegram.substring(0,
				messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
		Integer id = Integer.valueOf(done);

		try {
			ToDoItem item = getToDoItemById(id).getBody();
			if (item != null) {
				item.setDone(true);
				updateToDoItem(item, id);
				BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DONE.getMessage(), this);
			} else {
				logger.warn("Item with ID {} not found.", id);
				BotHelper.sendMessageToTelegram(chatId, "Item not found. Please try again.", this);
			}

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	private void undo(String messageTextFromTelegram, Long chatId) {

		String undo = messageTextFromTelegram.substring(0,
				messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
		Integer id = Integer.valueOf(undo);

		try {

			ToDoItem item = getToDoItemById(id).getBody();
			if (item != null) {
				item.setDone(false);
				updateToDoItem(item, id);
				BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_UNDONE.getMessage(), this);
			} else {
				logger.warn("Item with ID {} not found.", id);
				BotHelper.sendMessageToTelegram(chatId, "Item not found. Please try again.", this);
			}

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}

	}

	private void delete(String messageTextFromTelegram, Long chatId) {
		String delete = messageTextFromTelegram.substring(0,
				messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
		Integer id = Integer.valueOf(delete);

		try {

			deleteToDoItem(id).getBody();
			BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DELETED.getMessage(), this);

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	private void update(String messageTextFromTelegram, Long chatId) {
		String itemId = messageTextFromTelegram.substring(0,
				messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
		Integer id = Integer.valueOf(itemId);

		// Store that this user is updating this specific item
		userUpdatingItemMap.put(chatId, id);

		SendMessage messageToTelegram = new SendMessage();
		messageToTelegram.setChatId(chatId);
		messageToTelegram.setText(BotMessages.TYPE_UPDATE_TODO_ITEM.getMessage());

		// Add keyboard removal if needed
		ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
		messageToTelegram.setReplyMarkup(keyboardMarkup);

		try {
			execute(messageToTelegram);
		} catch (TelegramApiException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	private void allItems(String messageTextFromTelegram, Long chatId) {
		List<ToDoItem> allItems = getAllToDoItems();
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboard = new ArrayList<>();

		// command back to main screen
		KeyboardRow mainScreenRowTop = new KeyboardRow();
		mainScreenRowTop.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
		keyboard.add(mainScreenRowTop);

		KeyboardRow firstRow = new KeyboardRow();
		firstRow.add(BotLabels.ADD_NEW_ITEM.getLabel());
		keyboard.add(firstRow);

		KeyboardRow myTodoListTitleRow = new KeyboardRow();
		myTodoListTitleRow.add(BotLabels.MY_TODO_LIST.getLabel());
		keyboard.add(myTodoListTitleRow);

		List<ToDoItem> activeItems = allItems.stream().filter(item -> item.isDone() == false)
				.collect(Collectors.toList());

		for (ToDoItem item : activeItems) {

			KeyboardRow currentRow = new KeyboardRow();
			currentRow.add(item.getTitle());
			currentRow.add(item.getID() + BotLabels.DASH.getLabel() + BotLabels.DONE.getLabel());
			currentRow.add(item.getID() + BotLabels.DASH.getLabel() + BotLabels.UPDATE_ITEM.getLabel());
			keyboard.add(currentRow);
		}

		List<ToDoItem> doneItems = allItems.stream().filter(item -> item.isDone() == true)
				.collect(Collectors.toList());

		for (ToDoItem item : doneItems) {
			KeyboardRow currentRow = new KeyboardRow();
			currentRow.add(item.getTitle());
			currentRow.add(item.getID() + BotLabels.DASH.getLabel() + BotLabels.UNDO.getLabel());
			currentRow.add(item.getID() + BotLabels.DASH.getLabel() + BotLabels.DELETE.getLabel());
			currentRow.add(item.getID() + BotLabels.DASH.getLabel() + BotLabels.UPDATE_ITEM.getLabel());
			keyboard.add(currentRow);
		}

		// command back to main screen
		KeyboardRow mainScreenRowBottom = new KeyboardRow();
		mainScreenRowBottom.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
		keyboard.add(mainScreenRowBottom);

		keyboardMarkup.setKeyboard(keyboard);

		SendMessage messageToTelegram = new SendMessage();
		messageToTelegram.setChatId(chatId);
		messageToTelegram.setText(BotLabels.MY_TODO_LIST.getLabel());
		messageToTelegram.setReplyMarkup(keyboardMarkup);
		// send the list as a message with formatting

		messageToTelegram.setText(formatTodoList(allItems));

		try {
			execute(messageToTelegram);
		} catch (TelegramApiException e) {
			logger.error(e.getLocalizedMessage(), e);
			messageToTelegram.setText(BotMessages.ERROR.getMessage());
		}
	}

	private void addItem(String messageTextFromTelegram, Long chatId) {
		try {
			SendMessage messageToTelegram = new SendMessage();
			messageToTelegram.setChatId(chatId);
			messageToTelegram.setText(BotMessages.TYPE_NEW_TODO_ITEM.getMessage());
			// hide keyboard
			ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
			messageToTelegram.setReplyMarkup(keyboardMarkup);

			// send message
			execute(messageToTelegram);

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}

	}

	private void allUsers(String messageTextFromTelegram, Long chatId) {
		List<User> allUsers = userService.findAll();

		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboard = new ArrayList<>();

		// command back to main screen
		KeyboardRow mainScreenRowTop = new KeyboardRow();
		mainScreenRowTop.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
		keyboard.add(mainScreenRowTop);

		for (User user : allUsers) {
			// First row: User name
			KeyboardRow row = new KeyboardRow();
			row.add(user.getName());
			row.add(user.getID() + BotLabels.DASH.getLabel() + "KPI");
			row.add(user.getID() + BotLabels.DASH.getLabel() + "TASKS");
			keyboard.add(row);
		}

		// Set the keyboard
		keyboardMarkup.setKeyboard(keyboard);

		// Create message with formatted user list
		StringBuilder sb = new StringBuilder("All Users:\n\n");
		for (User user : allUsers) {
			sb.append(String.format("👤 %s\n", user.getName()))
					.append(String.format("ID: %s\n", user.getID()))
					.append("\n");
		}

		// Create and send message with keyboard
		SendMessage messageToTelegram = new SendMessage();
		messageToTelegram.setChatId(chatId);
		messageToTelegram.setText(sb.toString());
		messageToTelegram.setReplyMarkup(keyboardMarkup);

		try {
			execute(messageToTelegram);
		} catch (TelegramApiException e) {
			logger.error(e.getLocalizedMessage(), e);
			BotHelper.sendMessageToTelegram(chatId, BotMessages.ERROR.getMessage(), this);
		}
	}

	private void seeKpi(String messageTextFromTelegram, Long chatId) {
		try {
			// Extract the user ID from the message (format: "123-KPI")
			String userIdStr = messageTextFromTelegram.substring(0,
					messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
			Integer userId = Integer.valueOf(userIdStr);

			// Get user details
			User user = userService.getUserById(userId);
			if (user == null) {
				BotHelper.sendMessageToTelegram(chatId, "User not found.", this);
				return;
			}

			// get latest sprint
			Sprint latestSprint = sprintService.getLatestSprint();
			if (latestSprint == null) {
				BotHelper.sendMessageToTelegram(chatId, "No active sprint found.", this);
				return;
			}
			// Get the sprint ID
			Integer sprintId = latestSprint.getID();

			// Fetch KPI data for this user
			List<Kpi> userKpis = kpiService.getKpiSummary(userId, null, null, sprintId);

			// Format KPI data
			StringBuilder sb = new StringBuilder();

			//show sprint
			sb.append(String.format("📊 *KPI Summary for %s*\n\n", user.getName()))
					.append(String.format("Sprint: %s\n", latestSprint.getName()))
					.append("\n");

			// Group KPIs by type
			Map<String, Map<String, Integer>> kpiByType = new HashMap<>();
			kpiByType.put("VISIBILITY", new HashMap<>(Map.of("sum", 0, "total", 0)));
			kpiByType.put("ACCOUNTABILITY", new HashMap<>(Map.of("sum", 0, "total", 0)));
			kpiByType.put("PRODUCTIVITY", new HashMap<>(Map.of("sum", 0, "total", 0)));

			for (Kpi kpi : userKpis) {
				if (kpiByType.containsKey(kpi.getType())) {
					kpiByType.get(kpi.getType()).put("sum", kpiByType.get(kpi.getType()).get("sum") + kpi.getSum());
					kpiByType.get(kpi.getType()).put("total",
							kpiByType.get(kpi.getType()).get("total") + kpi.getTotal());
				}
			}

			// Format each KPI type
			sb.append("*Visibility*: ");
			formatKpiProgress(sb, kpiByType.get("VISIBILITY"));

			sb.append("\n*Accountability*: ");
			formatKpiProgress(sb, kpiByType.get("ACCOUNTABILITY"));

			sb.append("\n*Productivity*: ");
			formatKpiProgress(sb, kpiByType.get("PRODUCTIVITY"));

			// Create and send message with back button
			SendMessage messageToTelegram = new SendMessage();
			messageToTelegram.setChatId(chatId);
			messageToTelegram.setText(sb.toString());
			messageToTelegram.enableMarkdown(true);

			// Add keyboard with back button
			List<User> allUsers = userService.findAll();
			ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
			List<KeyboardRow> keyboard = new ArrayList<>();
	
			// command back to main screen
			KeyboardRow mainScreenRowTop = new KeyboardRow();
			mainScreenRowTop.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
			keyboard.add(mainScreenRowTop);
	
			for (User u : allUsers) {
				// First row: User name
				KeyboardRow row = new KeyboardRow();
				row.add(u.getName());
				row.add(u.getID() + BotLabels.DASH.getLabel() + "KPI");
				row.add(u.getID() + BotLabels.DASH.getLabel() + "TASKS");
				keyboard.add(row);
			}
	
			// Set the keyboard
			keyboardMarkup.setKeyboard(keyboard);
			messageToTelegram.setReplyMarkup(keyboardMarkup);

			execute(messageToTelegram);
		} catch (Exception e) {
			logger.error("Error displaying KPI data: " + e.getMessage(), e);
			BotHelper.sendMessageToTelegram(chatId, "Error retrieving KPI data.", this);
		}
	}

	private void seeUserSummary(String messageTextFromTelegram, Long chatId) {
		try {
			// Extract the user ID from the message (format: "123-TASKS")
			String userIdStr = messageTextFromTelegram.substring(0,
					messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
			Integer userId = Integer.valueOf(userIdStr);

			// Get user details
			User user = userService.getUserById(userId);
	
			List<ToDoItem> userTasks = toDoItemService.getToDoItemsByUserId(userId);
			
			// Format task data
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("📋 *Tasks for %s*\n\n", user.getName()));
			
			if (userTasks.isEmpty()) {
				sb.append("No tasks found for this user.");
			} else {
				// Group by active/done tasks
				List<ToDoItem> activeTasks = userTasks.stream()
					.filter(item -> !item.isDone())
					.collect(Collectors.toList());
					
				List<ToDoItem> completedTasks = userTasks.stream()
					.filter(ToDoItem::isDone)
					.collect(Collectors.toList());
				
				// Add active tasks section
				sb.append("*Active Tasks:*\n");
				if (activeTasks.isEmpty()) {
					sb.append("No active tasks.\n");
				} else {
					for (ToDoItem task : activeTasks) {
						sb.append(String.format("📌 %s\n", task.getTitle() != null ? task.getTitle() : "No title"))
						  .append(String.format("  Description: %s\n", task.getDescription() != null ? task.getDescription() : "No description"))
						  .append(String.format("  State: %s\n", task.getState() != null ? task.getState().getName() : "Not set"))
						  .append(String.format("  Story Points: %s\n", task.getStoryPoints() != null ? task.getStoryPoints() : "Not set"))
						  .append(String.format("  Estimated Hours: %s\n", task.getEstimatedHours() != null ? task.getEstimatedHours() : "Not set"))
						  .append(String.format("  Real Hours: %s\n", task.getRealHours() != null ? task.getRealHours() : "Not set"))
						  .append("\n");
					}
				}
				
				// Add completed tasks section
				sb.append("\n*Completed Tasks:*\n");
				if (completedTasks.isEmpty()) {
					sb.append("No completed tasks.\n");
				} else {
					for (ToDoItem task : completedTasks) {
						sb.append(String.format("✅ %s\n", task.getTitle() != null ? task.getTitle() : "No title"))
						  .append("\n");
					}
				}
			}
	
			// Create and send message with back button
			SendMessage messageToTelegram = new SendMessage();
			messageToTelegram.setChatId(chatId);
			messageToTelegram.setText(sb.toString());
			messageToTelegram.enableMarkdown(true);
	
			// Add keyboard with back button and other users
			List<User> allUsers = userService.findAll();
			ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
			List<KeyboardRow> keyboard = new ArrayList<>();
	
			// Command back to main screen
			KeyboardRow mainScreenRowTop = new KeyboardRow();
			mainScreenRowTop.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
			keyboard.add(mainScreenRowTop);
	
			for (User u : allUsers) {
				// First row: User name
				KeyboardRow row = new KeyboardRow();
				row.add(u.getName());
				row.add(u.getID() + BotLabels.DASH.getLabel() + "KPI");
				row.add(u.getID() + BotLabels.DASH.getLabel() + "TASKS");
				keyboard.add(row);
			}
	
			// Set the keyboard
			keyboardMarkup.setKeyboard(keyboard);
			messageToTelegram.setReplyMarkup(keyboardMarkup);
	
			execute(messageToTelegram);
		} catch (Exception e) {
			logger.error("Error displaying user tasks: " + e.getMessage(), e);
			BotHelper.sendMessageToTelegram(chatId, "Error retrieving user tasks.", this);
		}
	}

	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {

			String messageTextFromTelegram = update.getMessage().getText();
			long chatId = update.getMessage().getChatId();

			if (userUpdatingItemMap.containsKey(chatId)) {
				try {
					Integer itemId = userUpdatingItemMap.get(chatId);
					
					// Get the existing item
					ResponseEntity<ToDoItem> response = getToDoItemById(itemId);
					if (response.getStatusCode() == HttpStatus.OK) {
						ToDoItem existingItem = response.getBody();
						
						// Parse the update message and apply changes to the existing item
						updateItemFromMessage(existingItem, messageTextFromTelegram);
						
						// Update the item in the database
						ResponseEntity updateResponse = updateToDoItem(existingItem, itemId);
						
						// Clear the updating state
						userUpdatingItemMap.remove(chatId);
						
						// Inform the user that the update was successful
						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram.setText(BotMessages.ITEM_UPDATED.getMessage());
						execute(messageToTelegram);
					} else {
						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram.setText("Item not found. Please try again.");
						execute(messageToTelegram);
						userUpdatingItemMap.remove(chatId);
					}
					return; // Exit method after handling update
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
					
					// Inform the user of the error
					try {
						SendMessage errorMessage = new SendMessage();
						errorMessage.setChatId(chatId);
						errorMessage.setText("An error occurred: " + e.getMessage());
						execute(errorMessage);
						
						// Clear any updating state on error
						userUpdatingItemMap.remove(chatId);
					} catch (TelegramApiException telegramException) {
						logger.error("Failed to send error message", telegramException);
					}
					return; // Exit method after handling error
				}
			}

			if (messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand()) || messageTextFromTelegram.equals(BotLabels.SHOW_MAIN_SCREEN.getLabel())) {

				start(chatId);
			} else if (messageTextFromTelegram.indexOf(BotLabels.DONE.getLabel()) != -1) {
				done(messageTextFromTelegram, chatId);

			} else if (messageTextFromTelegram.indexOf(BotLabels.UNDO.getLabel()) != -1) {

				undo(messageTextFromTelegram, chatId);

			} else if (messageTextFromTelegram.indexOf(BotLabels.DELETE.getLabel()) != -1) {

				delete(messageTextFromTelegram, chatId);

			} else if (messageTextFromTelegram.indexOf(BotLabels.UPDATE_ITEM.getLabel()) != -1) {
				
				update(messageTextFromTelegram, chatId);
			
			} else if (messageTextFromTelegram.equals(BotCommands.HIDE_COMMAND.getCommand()) || messageTextFromTelegram.equals(BotLabels.HIDE_MAIN_SCREEN.getLabel())) {

				BotHelper.sendMessageToTelegram(chatId, BotMessages.BYE.getMessage(), this);

			} else if (messageTextFromTelegram.equals(BotCommands.TODO_LIST.getCommand()) || messageTextFromTelegram.equals(BotLabels.LIST_ALL_ITEMS.getLabel())|| messageTextFromTelegram.equals(BotLabels.MY_TODO_LIST.getLabel())) {

				allItems(messageTextFromTelegram, chatId);

			} else if (messageTextFromTelegram.equals(BotCommands.ADD_ITEM.getCommand()) || messageTextFromTelegram.equals(BotLabels.ADD_NEW_ITEM.getLabel())) {
				
				addItem(messageTextFromTelegram, chatId);

			} else if (messageTextFromTelegram.equals(BotCommands.USER_LIST.getCommand()) || messageTextFromTelegram.equals(BotLabels.LIST_ALL_USERS.getLabel())) {

				allUsers(messageTextFromTelegram, chatId);

			} else if (messageTextFromTelegram.indexOf("KPI") != -1 && messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()) != -1) {

	 			seeKpi(messageTextFromTelegram, chatId);
			} else if (messageTextFromTelegram.indexOf("TASKS") != -1 && messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()) != -1) {
				seeUserSummary(messageTextFromTelegram, chatId);
			}

			else {
				// Handle as a new item creation
				try {
					ToDoItem newItem = parseToDoItem(messageTextFromTelegram);
					ResponseEntity entity = addToDoItem(newItem);
					
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotMessages.NEW_ITEM_ADDED.getMessage());
					
					execute(messageToTelegram);
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
					
					// Inform the user of the error
					try {
						SendMessage errorMessage = new SendMessage();
						errorMessage.setChatId(chatId);
						errorMessage.setText("An error occurred: " + e.getMessage());
						execute(errorMessage);
					} catch (TelegramApiException telegramException) {
						logger.error("Failed to send error message", telegramException);
					}
				
				}
			}
		}
	}

	private void updateItemFromMessage(ToDoItem existingItem, String message) {
		// Don't update creation timestamp or ID

		// Check if the message contains commas for advanced parsing
		if (message.contains(",")) {
			String[] values = message.split(",");

			// Process each field based on its position
			for (int i = 0; i < values.length; i++) {
				String value = values[i].trim();

				// Skip empty values - those fields won't be updated
				if (value.isEmpty()) {
					continue;
				}

				// Update the appropriate field based on position
				switch (i) {
					case 0: // Title
						existingItem.setTitle(value);
						break;
					case 1: // Description
						existingItem.setDescription(value);
						break;
					case 2: // Due date (day-month-year)
						try {
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
							LocalDate localDate = LocalDate.parse(value, formatter);
							OffsetDateTime dueDate = localDate.atStartOfDay().atOffset(ZoneOffset.UTC);
							existingItem.setDueDate(dueDate);
						} catch (Exception e) {
							logger.error("Error parsing due date: " + value, e);
						}
						break;
					case 3: // State - need to find by name
						try {
							State state = this.stateService.getStateByName(value);
							existingItem.setState(state);
						} catch (Exception e) {
							logger.error("Error setting state: " + value, e);
						}

						break;
					case 4: // Sprint
						try {
							Sprint sprint = this.sprintService.getSprintById(Integer.parseInt(value));
							existingItem.setSprint(sprint);
						} catch (Exception e) {
							logger.error("Error setting sprint: " + value, e);
						}
						break;
					case 5: // User - need to find by name
						try {
							User user = this.userService.getUserByName(value);
							existingItem.setUser(user);
						} catch (Exception e) {
							logger.error("Error setting user: " + value, e);
						}
						break;
					case 6: // Story points
						try {
							int storyPoints = Integer.parseInt(value);
							existingItem.setStoryPoints(storyPoints);
						} catch (NumberFormatException e) {
							logger.error("Error parsing story points: " + value, e);
						}
						break;
					case 7: // Priority
						String priorityLower = value.toLowerCase();
						if (priorityLower.equals("low") || priorityLower.equals("medium")
								|| priorityLower.equals("high")) {
							existingItem.setPriority(priorityLower);
						} else {
							logger.warn("Invalid priority value: '{}'. Keeping existing value.", value);
						}
						break;
					case 8: // Estimated hours
						try {
							int estimatedHours = Integer.parseInt(value);
							existingItem.setEstimatedHours(estimatedHours);
						} catch (NumberFormatException e) {
							logger.error("Error parsing estimated hours: " + value, e);
						}
						break;
					case 9: // Real hours
						try {
							int realHours = Integer.parseInt(value);
							existingItem.setRealHours(realHours);
						} catch (NumberFormatException e) {
							logger.error("Error parsing real hours: " + value, e);
						}
						break;
					case 10: // Done status
						if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes") ||
								value.equalsIgnoreCase("done") || value.equalsIgnoreCase("1")) {
							existingItem.setDone(true);
						} else if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no") ||
								value.equalsIgnoreCase("pending") || value.equalsIgnoreCase("0")) {
							existingItem.setDone(false);
						}
						break;
				}
			}
		} else {
			// If only a simple message is sent, update the title
			existingItem.setTitle(message);
		}
	}

	// GET /todolist
	public List<ToDoItem> getAllToDoItems() {
		List<ToDoItem> items = toDoItemService.findAll();
		return items;
	}

	// GET BY ID /todolist/{id}
	public ResponseEntity<ToDoItem> getToDoItemById(@PathVariable int id) {
		try {
			ToDoItem item = toDoItemService.getItemById(id);
			ResponseEntity<ToDoItem> responseEntity = new ResponseEntity<>(item, HttpStatus.OK);
			return new ResponseEntity<ToDoItem>(responseEntity.getBody(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// PUT /todolist
	public ResponseEntity addToDoItem(@RequestBody ToDoItem todoItem) throws Exception {
		ToDoItem td = toDoItemService.addToDoItem(todoItem);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("location", "" + td.getID());
		responseHeaders.set("Access-Control-Expose-Headers", "location");
		// URI location = URI.create(""+td.getID())

		return ResponseEntity.ok().headers(responseHeaders).build();
	}

	// UPDATE /todolist/{id}
	public ResponseEntity updateToDoItem(@RequestBody ToDoItem toDoItem, @PathVariable int id) {
		try {
			ToDoItem toDoItem1 = toDoItemService.updateToDoItem(id, toDoItem);
			System.out.println(toDoItem1.toString());
			return new ResponseEntity<>(toDoItem1, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	// DELETE todolist/{id}
	public ResponseEntity<Boolean> deleteToDoItem(@PathVariable("id") int id) {
		Boolean flag = false;
		try {
			flag = toDoItemService.deleteToDoItem(id);
			return new ResponseEntity<>(flag, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
		}
	}

	private ToDoItem parseToDoItem(String message) {
		ToDoItem newItem = new ToDoItem();
		newItem.setCreation_ts(OffsetDateTime.now()); // Keep existing creation timestamp
		newItem.setDone(false); // Keep existing done status
		newItem.setDeleted(false); // Ensure it's not marked as deleted

		Project defaultProject = projectService.findAll().get(0);
		newItem.setProject(defaultProject);

		// Check if the message contains commas for advanced parsing
		if (message.contains(",")) {
			String[] values = message.split(",");

			// Process each field based on its position
			for (int i = 0; i < values.length; i++) {
				String value = values[i].trim();

				// Skip empty values
				if (value.isEmpty()) {
					continue;
				}

				// Set the appropriate field based on position
				switch (i) {
					case 0: // Title
						newItem.setTitle(value);
						break;
					case 1: // Description
						newItem.setDescription(value);
						break;
					case 2: // Due date (day-month-year)
						try {
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
							LocalDate localDate = LocalDate.parse(value, formatter);
							OffsetDateTime dueDate = localDate.atStartOfDay().atOffset(ZoneOffset.UTC);
							newItem.setDueDate(dueDate);
						} catch (Exception e) {
							logger.error("Error parsing due date: " + value, e);
						}
						break;
					case 3: // State - need to find by name
						try {
							State state = this.stateService.getStateByName(value);
							if (state != null) {
								newItem.setState(state);
							}
						} catch (Exception e) {
							logger.error("Error setting state: " + value, e);

						}
						break;
					case 4: // Sprint
						try {
							Sprint sprint = this.sprintService.getSprintById(Integer.parseInt(value));
							if (sprint != null) {
								newItem.setSprint(sprint);
							}
						} catch (Exception e) {
							logger.error("Error setting sprint: " + value, e);
						}
						break;
					case 5: // User - need to find by name
						try {
							User user = this.userService.getUserByName(value);
							if (user != null) {
								newItem.setUser(user);
							}
						} catch (Exception e) {
							logger.error("Error setting user: " + value, e);

						}
						break;
					case 6: // Story points
						try {
							int storyPoints = Integer.parseInt(value);
							newItem.setStoryPoints(storyPoints);
						} catch (NumberFormatException e) {
							logger.error("Error parsing story points: " + value, e);

						}
						break;
					case 7: // Priority
						String priorityLower = value.toLowerCase();
						if (priorityLower.equals("low") || priorityLower.equals("medium")
								|| priorityLower.equals("high")) {
							newItem.setPriority(priorityLower);
						} else {
							logger.warn("Invalid priority value: '{}'. Setting to 'medium'", value);
							newItem.setPriority("medium");
						}
						break;
					case 8: // Estimated hours
						try {
							int estimatedHours = Integer.parseInt(value);
							newItem.setEstimatedHours(estimatedHours);
						} catch (NumberFormatException e) {
							logger.error("Error parsing estimated hours: " + value, e);

						}
						break;
					case 9: // Real hours
						try {
							int realHours = Integer.parseInt(value);
							newItem.setRealHours(realHours);
						} catch (NumberFormatException e) {
							logger.error("Error parsing real hours: " + value, e);

						}
						break;
				}
			}
		} else {
			// If no commas found, use the existing behavior of just setting the title
			newItem.setTitle(message);
		}

		return newItem;
	}

}