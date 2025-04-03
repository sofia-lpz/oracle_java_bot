package com.springboot.MyTodoList.controller;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.State;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.service.StateService;
import com.springboot.MyTodoList.service.UserService;
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
	private String botName;

	public ToDoItemBotController(String botToken, String botName, ToDoItemService toDoItemService,
			StateService stateService, UserService userService, ProjectService projectService, SprintService sprintService) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.toDoItemService = toDoItemService;
		this.botName = botName;
		this.stateService = stateService;
		this.userService = userService;
		this.projectService = projectService;
		this.sprintService = sprintService;
	}

	private String formatTodoList(List<ToDoItem> items) {
		logger.debug("formatTodoList() called with {} items", items.size());

		if (items.isEmpty()) {
			return "No tasks found.";
		}
		
		StringBuilder sb = new StringBuilder("*Your Todo List:*\n\n");
		
		for (ToDoItem item : items) {
			sb.append(String.format("📌 *%s*\n", item.getTitle()))
			  .append(String.format("Description: %s\n", item.getDescription()))
			  .append(String.format("Status: %s\n", item.isDone() ? "✅ Done" : "⏳ Pending"))
			  .append("\n");
		}

		logger.debug("formatTodoList() returning: {}", sb.toString());
		
		return sb.toString();
	}

	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {

			String messageTextFromTelegram = update.getMessage().getText();
			long chatId = update.getMessage().getChatId();

			// START MESSAGE
			if (messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand())
					|| messageTextFromTelegram.equals(BotLabels.SHOW_MAIN_SCREEN.getLabel())) {

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotMessages.HELLO_MYTODO_BOT.getMessage());

				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				// first keyboard
				KeyboardRow row = new KeyboardRow();
				row.add(BotLabels.LIST_ALL_ITEMS.getLabel());
				row.add(BotLabels.ADD_NEW_ITEM.getLabel());
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

			} else if (messageTextFromTelegram.indexOf(BotLabels.DONE.getLabel()) != -1) {

				String done = messageTextFromTelegram.substring(0,
						messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
				Integer id = Integer.valueOf(done);

				try {

					ToDoItem item = getToDoItemById(id).getBody();
					item.setDone(true);
					updateToDoItem(item, id);
					BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DONE.getMessage(), this);

				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.indexOf(BotLabels.UNDO.getLabel()) != -1) {

				String undo = messageTextFromTelegram.substring(0,
						messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
				Integer id = Integer.valueOf(undo);

				try {

					ToDoItem item = getToDoItemById(id).getBody();
					item.setDone(false);
					updateToDoItem(item, id);
					BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_UNDONE.getMessage(), this);

				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.indexOf(BotLabels.DELETE.getLabel()) != -1) {

				String delete = messageTextFromTelegram.substring(0,
						messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
				Integer id = Integer.valueOf(delete);

				try {

					deleteToDoItem(id).getBody();
					BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DELETED.getMessage(), this);

				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.equals(BotCommands.HIDE_COMMAND.getCommand())
					|| messageTextFromTelegram.equals(BotLabels.HIDE_MAIN_SCREEN.getLabel())) {

				BotHelper.sendMessageToTelegram(chatId, BotMessages.BYE.getMessage(), this);

			} else if (messageTextFromTelegram.equals(BotCommands.TODO_LIST.getCommand())
					|| messageTextFromTelegram.equals(BotLabels.LIST_ALL_ITEMS.getLabel())
					|| messageTextFromTelegram.equals(BotLabels.MY_TODO_LIST.getLabel())) {

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
					keyboard.add(currentRow);
				}

				List<ToDoItem> doneItems = allItems.stream().filter(item -> item.isDone() == true)
						.collect(Collectors.toList());

				for (ToDoItem item : doneItems) {
					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getDescription());
					currentRow.add(item.getID() + BotLabels.DASH.getLabel() + BotLabels.UNDO.getLabel());
					currentRow.add(item.getID() + BotLabels.DASH.getLabel() + BotLabels.DELETE.getLabel());
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
				//send the list as a message with formatting

				messageToTelegram.setText(formatTodoList(allItems));

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
					messageToTelegram.setText(BotMessages.ERROR.getMessage());
				}

			} else if (messageTextFromTelegram.equals(BotCommands.ADD_ITEM.getCommand())
					|| messageTextFromTelegram.equals(BotLabels.ADD_NEW_ITEM.getLabel())) {
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

			else {
				try{
					ToDoItem newItem = parseToDoItem(messageTextFromTelegram);
					ResponseEntity entity = addToDoItem(newItem);
			
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotMessages.NEW_ITEM_ADDED.getMessage());
			
					execute(messageToTelegram);
				}catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}
		}
	}

	@Override
	public String getBotUsername() {		
		return botName;
	}

	// GET /todolist
	public List<ToDoItem> getAllToDoItems() { 
		List<ToDoItem> items = toDoItemService.findAll();
		logger.debug("getAllToDoItems() found {} items", items.size());
		return items;
	}
	// GET BY ID /todolist/{id}
	public ResponseEntity<ToDoItem> getToDoItemById(@PathVariable int id) {
		try {
			ResponseEntity<ToDoItem> responseEntity = toDoItemService.getItemById(id);
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
						ResponseEntity<State> responseEntity = this.stateService.getStateByName(value);
						if (responseEntity != null && responseEntity.getBody() != null) {
							State state = responseEntity.getBody();
							newItem.setState(state);
						}
                    } catch (Exception e) {
                        logger.error("Error setting state: " + value, e);
				
                    }
                    break;
                case 4: // Sprint
					try {
						ResponseEntity<Sprint> responseEntity = this.sprintService.getSprintById(Integer.parseInt(value));
						if (responseEntity != null && responseEntity.getBody() != null) {
							Sprint sprint = responseEntity.getBody();
							newItem.setSprint(sprint);
						}
					} catch (Exception e) {
						logger.error("Error setting sprint: " + value, e);
					}
					break;
                case 5: // User - need to find by name
					try {
						ResponseEntity<User> responseEntity = this.userService.getUserByName(value);
						if (responseEntity != null && responseEntity.getBody() != null) {
							User user = responseEntity.getBody();
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
					if (priorityLower.equals("low") || priorityLower.equals("medium") || priorityLower.equals("high")) {
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

// Helper methods to get entity references - these would need to use your service layer
private State getStateByName(String stateName) {
    // This is a placeholder - you'll need to implement this based on your service layer
    // Example implementation:
    //return StateService.getStateByName(stateName);
	return null;
}

private Sprint getSprintByName(String sprintName) {
    // This is a placeholder - you'll need to implement this based on your service layer
    // Example implementation:
    // return sprintService.findByName(sprintName);
    logger.debug("Attempting to find sprint with name: {}", sprintName);
    return null;
}

private User getUserByName(String userName) {
    // This is a placeholder - you'll need to implement this based on your service layer
    // Example implementation:
    // return userService.findByName(userName);
    logger.debug("Attempting to find user with name: {}", userName);
    return null;
}

private Project getProjectByName(String projectName) {
    // This is a placeholder - you'll need to implement this based on your service layer
    // Example implementation:
    // return projectService.findByName(projectName);
    logger.debug("Attempting to find project with name: {}", projectName);
    return null;
}

}


