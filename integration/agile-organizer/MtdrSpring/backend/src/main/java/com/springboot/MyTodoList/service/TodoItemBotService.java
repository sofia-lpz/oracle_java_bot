package com.springboot.MyTodoList.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import com.springboot.MyTodoList.model.Kpi;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.State;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.model.dto.LoginUserDto;
import com.springboot.MyTodoList.service.util.TelegramFormaterUtil;
import com.springboot.MyTodoList.util.BotLabels;
import com.springboot.MyTodoList.util.BotMessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoItemBotService {

    private static final Logger logger = LoggerFactory.getLogger(TodoItemBotService.class);

    private ToDoItemService toDoItemService;
    private UserService userService;
    private KpiService kpiService;
    private StateService stateService;
    private SprintService sprintService;
    private ProjectService projectService;
    private AuthenticationService authService;

    @Autowired
    public TodoItemBotService(ToDoItemService toDoItemService, UserService userService, KpiService kpiService,
            StateService stateService, SprintService sprintService, ProjectService projectService, AuthenticationService authService) {
        this.toDoItemService = toDoItemService;
        this.userService = userService;
        this.kpiService = kpiService;
        this.stateService = stateService;
        this.sprintService = sprintService;
        this.projectService = projectService;
        this.authService = authService;
    }

    public SendMessage startLogin() {
        SendMessage messageToTelegram = new SendMessage();
        messageToTelegram.setText(BotMessages.LOGIN.getMessage());

        ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
        messageToTelegram.setReplyMarkup(keyboardMarkup);

        return messageToTelegram;
    }

    public SendMessage start() {
        SendMessage messageToTelegram = new SendMessage();
        messageToTelegram.setText(BotMessages.HELLO_MYTODO_BOT.getMessage());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(BotLabels.LIST_ALL_ITEMS.getLabel());
        row.add(BotLabels.ADD_NEW_ITEM.getLabel());
        row.add(BotLabels.LIST_ALL_USERS.getLabel());
        keyboard.add(row);

        row = new KeyboardRow();
        row.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
        row.add(BotLabels.HIDE_MAIN_SCREEN.getLabel());
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);

        messageToTelegram.setReplyMarkup(keyboardMarkup);

        return messageToTelegram;
    }

    public SendMessage done(String messageTextFromTelegram) {
        String done = messageTextFromTelegram.substring(0,
                messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
        Integer id = Integer.valueOf(done);

        ToDoItem item = toDoItemService.getItemById(id);
        item.setDone(true);
        toDoItemService.updateToDoItem(id, item);

        SendMessage messageToTelegram = new SendMessage();
        messageToTelegram.setText(BotMessages.ITEM_DONE.getMessage());

        return messageToTelegram;
    }

    public SendMessage undo(String messageTextFromTelegram) {
        String undo = messageTextFromTelegram.substring(0,
                messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
        Integer id = Integer.valueOf(undo);
        SendMessage messageToTelegram = new SendMessage();

        ToDoItem item = toDoItemService.getItemById(id);
        if (item != null) {
            item.setDone(false);
            toDoItemService.updateToDoItem(id, item);
            messageToTelegram.setText(BotMessages.ITEM_UNDONE.getMessage());
            return messageToTelegram;
        } else {
            messageToTelegram.setText("Item not found. Please try again.");
            return messageToTelegram;
        }
    }

    public SendMessage delete(String messageTextFromTelegram) {
        String delete = messageTextFromTelegram.substring(0,
                messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
        Integer id = Integer.valueOf(delete);
        SendMessage messageToTelegram = new SendMessage();

        try {
            toDoItemService.deleteToDoItem(id);
            messageToTelegram.setText(BotMessages.ITEM_DELETED.getMessage());

        } catch (Exception e) {
            messageToTelegram.setText("Error deleting item. Please try again.");
        }
        return messageToTelegram;
    }

    public SendMessage allUsers(String messageTextFromTelegram) {
        List<User> allUsers = userService.findAll();

        SendMessage messageToTelegram = new SendMessage();

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
        messageToTelegram.setText(sb.toString());
        messageToTelegram.setReplyMarkup(keyboardMarkup);

        return messageToTelegram;
    }

    public SendMessage seeKpi(String messageTextFromTelegram) {
        // Extract the user ID from the message (format: "123-KPI")
        String userIdStr = messageTextFromTelegram.substring(0,
                messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
        Integer userId = Integer.valueOf(userIdStr);
        SendMessage messageToTelegram = new SendMessage();

        User user = userService.getUserById(userId);
        if (user == null) {
            messageToTelegram.setText("User not found.");
            return messageToTelegram;
        }

        Sprint latestSprint = sprintService.getLatestSprint();
        if (latestSprint == null) {
            messageToTelegram.setText("No active sprint found.");
            return messageToTelegram;
        }

        Integer sprintId = latestSprint.getID();

        List<Kpi> userKpis = kpiService.getKpiSummary(List.of(userId), Collections.emptyList(),
                Collections.emptyList(), List.of(sprintId));

        StringBuilder sb = new StringBuilder();

        // show sprint
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
        sb.append(TelegramFormaterUtil.formatKpiProgress(kpiByType.get("VISIBILITY")));

        sb.append("\n*Accountability*: ");
        sb.append(TelegramFormaterUtil.formatKpiProgress(kpiByType.get("ACCOUNTABILITY")));

        sb.append("\n*Productivity*: ");
        sb.append(TelegramFormaterUtil.formatKpiProgress(kpiByType.get("PRODUCTIVITY")));

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

        return messageToTelegram;
    }

    public SendMessage seeUserSummary(String messageTextFromTelegram) {

        // Create and send message with back button
        SendMessage messageToTelegram = new SendMessage();

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
                            .append(String.format("  Description: %s\n",
                                    task.getDescription() != null ? task.getDescription() : "No description"))
                            .append(String.format("  State: %s\n",
                                    task.getState() != null ? task.getState().getName() : "Not set"))
                            .append(String.format("  Story Points: %s\n",
                                    task.getStoryPoints() != null ? task.getStoryPoints() : "Not set"))
                            .append(String.format("  Estimated Hours: %s\n",
                                    task.getEstimatedHours() != null ? task.getEstimatedHours() : "Not set"))
                            .append(String.format("  Real Hours: %s\n",
                                    task.getRealHours() != null ? task.getRealHours() : "Not set"))
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

        return messageToTelegram;

    }

    public SendMessage allItems(String messageTextFromTelegram) {

        SendMessage messageToTelegram = new SendMessage();
        List<ToDoItem> allItems = toDoItemService.findAll();
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

        // Format the todo list
        String formattedList = TelegramFormaterUtil.formatTodoList(allItems);

        messageToTelegram.setReplyMarkup(keyboardMarkup);
        messageToTelegram.setText(formattedList);
        return messageToTelegram;

    }

    public SendMessage update(String messageTextFromTelegram) {
        String itemId = messageTextFromTelegram.substring(0,
                messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
        Integer id = Integer.valueOf(itemId);

        ToDoItem item = toDoItemService.getItemById(id);
        if (item == null) {
            SendMessage messageToTelegram = new SendMessage();
            messageToTelegram.setText("Item not found. Please try again.");
            return messageToTelegram;
        }

        SendMessage messageToTelegram = new SendMessage();

        messageToTelegram.setText(BotMessages.TYPE_UPDATE_TODO_ITEM.getMessage());

        ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
        messageToTelegram.setReplyMarkup(keyboardMarkup);
        return messageToTelegram;
    }

    public SendMessage addItem(String messageTextFromTelegram) {
        SendMessage messageToTelegram = new SendMessage();
        messageToTelegram.setText(BotMessages.TYPE_NEW_TODO_ITEM.getMessage());

        ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
        messageToTelegram.setReplyMarkup(keyboardMarkup);

        return messageToTelegram;
    }

    public SendMessage createItemFromMessage(String messageTextFromTelegram) {
        SendMessage messageToTelegram = new SendMessage();
        ToDoItem newItem = parseToDoItem(messageTextFromTelegram);

        if (newItem.getTitle() == null || newItem.getTitle().isEmpty()) {
            messageToTelegram.setText("Title is required. Please try again.");
            return messageToTelegram;
        }

        // Save the new item
        ToDoItem savedItem = toDoItemService.addToDoItem(newItem);
        if (savedItem != null) {
            messageToTelegram.setText("New item added successfully: " + savedItem.getTitle());
        } else {
            messageToTelegram.setText("Failed to add new item. Please try again.");
        }

        return messageToTelegram;
    }

    public SendMessage updateItemFromMessage(Integer existingItemId, String messageTextFromTelegram) {
        SendMessage messageToTelegram = new SendMessage();
        ToDoItem existingItem = toDoItemService.getItemById(existingItemId);

        if (existingItem == null) {
            messageToTelegram.setText("Item not found. Please try again.");
            return messageToTelegram;
        }

        if (messageTextFromTelegram.contains(",")) {
            String[] values = messageTextFromTelegram.split(",");
            boolean hasUpdates = false;

            // Process each field based on its position
            for (int i = 0; i < values.length; i++) {
                String value = values[i].trim();

                // Skip empty values - those fields won't be updated
                if (value.isEmpty()) {
                    continue;
                }

                switch (i) {
                    case 0: // Title
                        existingItem.setTitle(value);
                        hasUpdates = true;
                        break;
                    case 1: // Description
                        existingItem.setDescription(value);
                        hasUpdates = true;
                        break;
                    case 2: // Due date (day-month-year)
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            LocalDate localDate = LocalDate.parse(value, formatter);
                            OffsetDateTime dueDate = localDate.atStartOfDay().atOffset(ZoneOffset.UTC);
                            existingItem.setDueDate(dueDate);
                            hasUpdates = true;
                        } catch (Exception e) {
                            messageToTelegram.setText("Invalid date format. Please use dd-MM-yyyy.");
                            return messageToTelegram;
                        }
                        break;
                    case 3: // State - need to find by name
                        try {
                            State state = stateService.getStateByName(value);
                            existingItem.setState(state);
                            hasUpdates = true;
                        } catch (Exception e) {
                            messageToTelegram.setText("Invalid state name. Please try again.");
                            return messageToTelegram;
                        }
                        break;
                    case 4: // Sprint
                        try {
                            Sprint sprint = sprintService.getSprintById(Integer.parseInt(value));
                            existingItem.setSprint(sprint);
                            hasUpdates = true;
                        } catch (Exception e) {
                            messageToTelegram.setText("Invalid sprint ID. Please try again.");
                            return messageToTelegram;
                        }
                        break;
                    case 5: // User - need to find by name
                        try {
                            User user = userService.getUserByName(value);
                            existingItem.setUser(user);
                            hasUpdates = true;
                        } catch (Exception e) {
                            messageToTelegram.setText("Invalid user name. Please try again.");
                            return messageToTelegram;
                        }
                        break;
                    case 6: // Story points
                        try {
                            int storyPoints = Integer.parseInt(value);
                            existingItem.setStoryPoints(storyPoints);
                            hasUpdates = true;
                        } catch (NumberFormatException e) {
                            messageToTelegram.setText("Invalid story points value. Please enter a number.");
                            return messageToTelegram;
                        }
                        break;
                    case 7: // Priority
                        String priorityLower = value.toLowerCase();
                        if (priorityLower.equals("low") || priorityLower.equals("medium")
                                || priorityLower.equals("high")) {
                            existingItem.setPriority(priorityLower);
                            hasUpdates = true;
                        } else {
                            messageToTelegram.setText("Invalid priority value. Please use low, medium, or high.");
                            return messageToTelegram;
                        }
                        break;
                    case 8: // Estimated hours
                        try {
                            int estimatedHours = Integer.parseInt(value);
                            existingItem.setEstimatedHours(estimatedHours);
                            hasUpdates = true;
                        } catch (NumberFormatException e) {
                            messageToTelegram.setText("Invalid estimated hours value. Please enter a number.");
                            return messageToTelegram;
                        }
                        break;
                    case 9: // Real hours
                        try {
                            int realHours = Integer.parseInt(value);
                            existingItem.setRealHours(realHours);
                            hasUpdates = true;
                        } catch (NumberFormatException e) {
                            messageToTelegram.setText("Invalid real hours value. Please enter a number.");
                            return messageToTelegram;
                        }
                        break;
                    case 10: // Done status
                        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes") ||
                                value.equalsIgnoreCase("done") || value.equalsIgnoreCase("1")) {
                            existingItem.setDone(true);
                            hasUpdates = true;
                        } else if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no") ||
                                value.equalsIgnoreCase("pending") || value.equalsIgnoreCase("0")) {
                            existingItem.setDone(false);
                            hasUpdates = true;
                        }
                        break;
                }
            }

            // Check if any updates were made
            if (hasUpdates) {
                messageToTelegram.setText("Item updated successfully.");
            } else {
                messageToTelegram.setText("No valid updates provided.");
            }
            return messageToTelegram;

        } else {
            // If only a simple message is sent, update the title
            existingItem.setTitle(messageTextFromTelegram.trim());
            messageToTelegram.setText("Title updated to: " + existingItem.getTitle());
            return messageToTelegram;
        }
    }

    public SendMessage loginFromMessage(String messageTextFromTelegram, Long telegramChatId) {
        SendMessage messageToTelegram = new SendMessage();
        try {
            LoginUserDto loginUserDto = parseLoginUserDto(messageTextFromTelegram, telegramChatId);

            User authenticaUser = authService.authenticate(loginUserDto);

            if (authenticaUser != null) {
                messageToTelegram.setText(BotMessages.LOGIN_SUCCESS.getMessage());
            } else {
                messageToTelegram.setText(BotMessages.LOGIN_FAILED.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error during login: ", e);
            messageToTelegram.setText(BotMessages.LOGIN_FAILED.getMessage());
        }

        return messageToTelegram;
    }

    public SendMessage logout(Long telegramChatId) {
        SendMessage messageToTelegram = new SendMessage();
        try {
            authService.telegramLogout(telegramChatId);
            messageToTelegram.setText(BotMessages.LOGOUT_SUCCESS.getMessage());
        } catch (Exception e) {
            logger.error("Error during logout: ", e);
            messageToTelegram.setText(BotMessages.LOGOUT_FAILED.getMessage());
        }
        return messageToTelegram;
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
            newItem.setTitle(message);
        }

        return newItem;
    }

    private LoginUserDto parseLoginUserDto(String message, Long telegramChatId) {
        String[] parts = message.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid login format. Use: PhoneNumber, Password, RememberMe(0/1)");
        }
        String phoneNumber = parts[0].trim();
        String password = parts[1].trim();
        Integer rememberMeValue = Integer.parseInt(parts[2].trim());
        Long chatID = null;
        if (rememberMeValue == 1){
            chatID = telegramChatId;
        }

        return new LoginUserDto(phoneNumber, password, chatID);
    }
}