package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.*;
import com.springboot.MyTodoList.service.*;
import com.springboot.MyTodoList.util.BotCommands;
import com.springboot.MyTodoList.util.BotLabels;
import com.springboot.MyTodoList.util.BotMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ToDoItemBotControllerTest {
    
    @Mock
    private ToDoItemService toDoItemService;
    
    @Mock
    private StateService stateService;
    
    @Mock
    private UserService userService;
    
    @Mock
    private ProjectService projectService;
    
    @Mock
    private SprintService sprintService;
    
    @Mock
    private KpiService kpiService;
    
    @Mock
    private TeamService teamService;
    
    private ToDoItemBotController botController;
    
    private final String TOKEN = "test_token";
    private final String BOT_NAME = "7527480199:AAHkFSxAr8DgLQRXKlB5Z-B7DUCu6vLXSNc";
    private final long CHAT_ID = 123456789L;
    
    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        // First create the controller with mocked services
        botController = new ToDoItemBotController(
            TOKEN, 
            BOT_NAME, 
            toDoItemService, 
            stateService, 
            userService, 
            projectService, 
            sprintService,
            kpiService,
            teamService
        );
        
        // Then wrap it with a spy
        botController = spy(botController);
        
        // Configure specific methods to avoid external API calls
        doReturn(null).when(botController).execute(any(SendMessage.class));
    }
    
    @Test
    public void testGetBotUsername() {
        assertEquals(BOT_NAME, botController.getBotUsername());
    }
    
    @Test
    public void testStartCommand() {
        // Create update object with start command
        Update update = createUpdateWithMessage(BotCommands.START_COMMAND.getCommand());
        
        // Process the update
        botController.onUpdateReceived(update);
        
        // Verify that execute was called with appropriate parameters
        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        try {
            verify(botController, times(1)).execute(messageCaptor.capture());
            SendMessage sentMessage = messageCaptor.getValue();
            assertEquals(String.valueOf(CHAT_ID), sentMessage.getChatId());
            assertEquals(BotMessages.HELLO_MYTODO_BOT.getMessage(), sentMessage.getText());
            assertTrue(sentMessage.getReplyMarkup() instanceof ReplyKeyboardMarkup);
        } catch (TelegramApiException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testDoneCommand() {
        // Configure mock todo item
        ToDoItem item = new ToDoItem();
        item.setID(1);
        item.setTitle("Test Item");
        item.setDone(false);
        
        // Use doReturn for spied methods
        ResponseEntity<ToDoItem> responseEntity = new ResponseEntity<>(item, HttpStatus.OK);
        doReturn(responseEntity).when(botController).getToDoItemById(1);
        
        // Mock the updateToDoItem method to prevent NullPointerException
        ResponseEntity<Object> updateResponse = new ResponseEntity<>(item, HttpStatus.OK);
        doReturn(updateResponse).when(botController).updateToDoItem(any(ToDoItem.class), eq(1));
        
        // Create update object with done command
        Update update = createUpdateWithMessage("1-DONE");
        
        // Process the update
        botController.onUpdateReceived(update);
        
        // Verify the item was updated - using doReturn syntax for spy
        verify(botController).updateToDoItem(argThat(todoItem -> 
            todoItem.getID() == 1 && todoItem.isDone()), eq(1));
        
        // Verify success message was sent
        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        try {
            verify(botController, atLeastOnce()).execute(messageCaptor.capture());
            List<SendMessage> sentMessages = messageCaptor.getAllValues();
            boolean foundSuccessMessage = false;
            for (SendMessage message : sentMessages) {
                if (message.getText().equals(BotMessages.ITEM_DONE.getMessage())) {
                    foundSuccessMessage = true;
                    break;
                }
            }
            assertTrue(foundSuccessMessage, "Success message was not sent");
        } catch (TelegramApiException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testUndoCommand() {
        // Configure mock todo item
        ToDoItem item = new ToDoItem();
        item.setID(1);
        item.setTitle("Test Item");
        item.setDone(true);
        
        // Use doReturn for spied methods
        ResponseEntity<ToDoItem> responseEntity = new ResponseEntity<>(item, HttpStatus.OK);
        doReturn(responseEntity).when(botController).getToDoItemById(1);
        
        // Mock the updateToDoItem method to prevent NullPointerException
        ResponseEntity<Object> updateResponse = new ResponseEntity<>(item, HttpStatus.OK);
        doReturn(updateResponse).when(botController).updateToDoItem(any(ToDoItem.class), eq(1));
        
        // Create update object with undo command
        Update update = createUpdateWithMessage("1-UNDO");
        
        // Process the update
        botController.onUpdateReceived(update);
        
        // Verify the item was updated
        verify(botController).updateToDoItem(argThat(todoItem -> 
            todoItem.getID() == 1 && !todoItem.isDone()), eq(1));
        
        // Verify success message was sent
        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        try {
            verify(botController, atLeastOnce()).execute(messageCaptor.capture());
            List<SendMessage> sentMessages = messageCaptor.getAllValues();
            boolean foundSuccessMessage = false;
            for (SendMessage message : sentMessages) {
                if (message.getText().equals(BotMessages.ITEM_UNDONE.getMessage())) {
                    foundSuccessMessage = true;
                    break;
                }
            }
            assertTrue(foundSuccessMessage, "Success message was not sent");
        } catch (TelegramApiException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testDeleteCommand() {
        // Use doReturn for spied methods
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(true, HttpStatus.OK);
        doReturn(responseEntity).when(botController).deleteToDoItem(1);
        
        // Create update object with delete command
        Update update = createUpdateWithMessage("1-DELETE");
        
        // Process the update
        botController.onUpdateReceived(update);
        
        // Verify the item was deleted
        verify(botController).deleteToDoItem(1);
        
        // Verify success message was sent
        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        try {
            verify(botController, atLeastOnce()).execute(messageCaptor.capture());
            List<SendMessage> sentMessages = messageCaptor.getAllValues();
            boolean foundSuccessMessage = false;
            for (SendMessage message : sentMessages) {
                if (message.getText().equals(BotMessages.ITEM_DELETED.getMessage())) {
                    foundSuccessMessage = true;
                    break;
                }
            }
            assertTrue(foundSuccessMessage, "Success message was not sent");
        } catch (TelegramApiException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testUpdateCommand() {
        // Create update object with update command
        Update update = createUpdateWithMessage("1-UPDATE");
        
        // Process the update
        botController.onUpdateReceived(update);
        
        // Verify that the update message was sent
        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        try {
            verify(botController, times(1)).execute(messageCaptor.capture());
            SendMessage sentMessage = messageCaptor.getValue();
            assertEquals(String.valueOf(CHAT_ID), sentMessage.getChatId());
            assertEquals(BotMessages.TYPE_UPDATE_TODO_ITEM.getMessage(), sentMessage.getText());
            assertTrue(sentMessage.getReplyMarkup() instanceof ReplyKeyboardRemove);
        } catch (TelegramApiException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testUpdateItemProcess() {
        // First, set up the update state
        Update updateCommand = createUpdateWithMessage("1-UPDATE");
        botController.onUpdateReceived(updateCommand);
        
        // Configure mock todo item
        ToDoItem item = new ToDoItem();
        item.setID(1);
        item.setTitle("Old Title");
        
        // Setup the mock response - using doReturn for spied methods
        ResponseEntity<ToDoItem> getResponse = new ResponseEntity<>(item, HttpStatus.OK);
        doReturn(getResponse).when(botController).getToDoItemById(1);
        
        ResponseEntity<Object> updateResponse = new ResponseEntity<>(item, HttpStatus.OK);
        doReturn(updateResponse).when(botController).updateToDoItem(any(ToDoItem.class), eq(1));
        
        // Now send an update message
        Update updateMessage = createUpdateWithMessage("New Title, Updated Description");
        botController.onUpdateReceived(updateMessage);
        
        // Verify the item was updated with correct values
        ArgumentCaptor<ToDoItem> itemCaptor = ArgumentCaptor.forClass(ToDoItem.class);
        verify(botController).updateToDoItem(itemCaptor.capture(), eq(1));
        ToDoItem updatedItem = itemCaptor.getValue();
        assertEquals("New Title", updatedItem.getTitle());
        assertEquals("Updated Description", updatedItem.getDescription());
        
        // Verify success message was sent
        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        try {
            verify(botController, atLeastOnce()).execute(messageCaptor.capture());
            List<SendMessage> sentMessages = messageCaptor.getAllValues();
            boolean foundSuccessMessage = false;
            for (SendMessage message : sentMessages) {
                if (message.getText().equals(BotMessages.ITEM_UPDATED.getMessage())) {
                    foundSuccessMessage = true;
                    break;
                }
            }
            assertTrue(foundSuccessMessage, "Success message was not sent");
        } catch (TelegramApiException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testShowAllItems() {
        // Configure mock items
        ToDoItem item1 = new ToDoItem();
        item1.setID(1);
        item1.setTitle("Item 1");
        item1.setDescription("Description 1");
        item1.setDone(false);
        
        ToDoItem item2 = new ToDoItem();
        item2.setID(2);
        item2.setTitle("Item 2");
        item2.setDescription("Description 2");
        item2.setDone(true);
        
        List<ToDoItem> items = Arrays.asList(item1, item2);
        
        // Setup mock response - using doReturn for spied methods
        doReturn(items).when(botController).getAllToDoItems();
        
        // Create update object with list command
        Update update = createUpdateWithMessage(BotLabels.LIST_ALL_ITEMS.getLabel());
        
        // Process the update
        botController.onUpdateReceived(update);
        
        // Verify that the items list was sent
        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        try {
            verify(botController, atLeastOnce()).execute(messageCaptor.capture());
            List<SendMessage> sentMessages = messageCaptor.getAllValues();
            boolean foundItemsList = false;
            for (SendMessage message : sentMessages) {
                if (message.getText().contains("Your Todo List") && 
                    message.getText().contains("Item 1") && 
                    message.getText().contains("Item 2")) {
                    foundItemsList = true;
                    break;
                }
            }
            assertTrue(foundItemsList, "Todo list was not sent");
        } catch (TelegramApiException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testAddNewItem() throws Exception {
        // Configure mock project for new items
        Project defaultProject = new Project();
        defaultProject.setID(1);
        defaultProject.setName("Default Project");
        
        List<Project> projects = Arrays.asList(defaultProject);
        when(projectService.findAll()).thenReturn(projects);
        
        // Setup mock response for adding item
        ToDoItem newItem = new ToDoItem();
        newItem.setID(1);
        newItem.setTitle("New Item");
        ResponseEntity<ToDoItem> responseEntity = new ResponseEntity<>(newItem, HttpStatus.OK);
        doReturn(responseEntity).when(botController).addToDoItem(any(ToDoItem.class));
        
        // Create update object with new item text
        Update update = createUpdateWithMessage("New Task Title, Task Description");
        
        // Process the update
        botController.onUpdateReceived(update);
        
        // Verify that the new item was added
        verify(botController).addToDoItem(argThat(item -> 
            item.getTitle().equals("New Task Title") && 
            item.getDescription().equals("Task Description")));
        
        // Verify success message was sent
        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        try {
            verify(botController, atLeastOnce()).execute(messageCaptor.capture());
            List<SendMessage> sentMessages = messageCaptor.getAllValues();
            boolean foundSuccessMessage = false;
            for (SendMessage message : sentMessages) {
                if (message.getText().equals(BotMessages.NEW_ITEM_ADDED.getMessage())) {
                    foundSuccessMessage = true;
                    break;
                }
            }
            assertTrue(foundSuccessMessage, "Success message was not sent");
        } catch (TelegramApiException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testShowAllUsers() {
        // Configure mock users
        User user1 = new User();
        user1.setID(1);
        user1.setName("User One");
        
        User user2 = new User();
        user2.setID(2);
        user2.setName("User Two");
        
        List<User> users = Arrays.asList(user1, user2);
        
        // Setup mock response
        when(userService.findAll()).thenReturn(users);
        
        // Create update object with user list command
        Update update = createUpdateWithMessage(BotLabels.LIST_ALL_USERS.getLabel());
        
        // Process the update
        botController.onUpdateReceived(update);
        
        // Verify that the users list was sent
        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        try {
            verify(botController, times(1)).execute(messageCaptor.capture());
            SendMessage sentMessage = messageCaptor.getValue();
            assertTrue(sentMessage.getText().contains("User One"));
            assertTrue(sentMessage.getText().contains("User Two"));
            assertTrue(sentMessage.getReplyMarkup() instanceof ReplyKeyboardMarkup);
        } catch (TelegramApiException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testSeeUserTasks() {
        // Configure mock user
        User user = new User();
        user.setID(1);
        user.setName("Test User");
        
        // Configure mock tasks
        ToDoItem task1 = new ToDoItem();
        task1.setID(1);
        task1.setTitle("Active Task");
        task1.setDescription("Task in progress");
        task1.setDone(false);
        
        State state = new State();
        state.setName("In Progress");
        task1.setState(state);
        
        ToDoItem task2 = new ToDoItem();
        task2.setID(2);
        task2.setTitle("Completed Task");
        task2.setDone(true);
        
        List<ToDoItem> tasks = Arrays.asList(task1, task2);
        List<User> users = Arrays.asList(user);
        
        // Setup mock responses
        when(userService.getUserById(1)).thenReturn(user);
        when(toDoItemService.getToDoItemsByUserId(1)).thenReturn(tasks);
        when(userService.findAll()).thenReturn(users);
        
        // Create update object with tasks command
        Update update = createUpdateWithMessage("1-TASKS");
        
        // Process the update
        botController.onUpdateReceived(update);
        
        // Verify that the tasks data was sent
        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        try {
            verify(botController, times(1)).execute(messageCaptor.capture());
            SendMessage sentMessage = messageCaptor.getValue();
            assertTrue(sentMessage.getText().contains("Tasks for Test User"));
            assertTrue(sentMessage.getText().contains("Active Tasks"));
            assertTrue(sentMessage.getText().contains("Active Task"));
            assertTrue(sentMessage.getText().contains("In Progress"));
            assertTrue(sentMessage.getText().contains("Completed Tasks"));
            assertTrue(sentMessage.getText().contains("Completed Task"));
            assertTrue(sentMessage.getReplyMarkup() instanceof ReplyKeyboardMarkup);
        } catch (TelegramApiException e) {
            fail("Exception should not be thrown");
        }
    }
    
    // Helper method to create Update objects for testing
    private Update createUpdateWithMessage(String text) {
        Update update = new Update();
        Message message = new Message();
        
        Chat chat = new Chat();
        chat.setId(CHAT_ID);
        
        message.setChat(chat);
        message.setText(text);
        
        update.setMessage(message);
        
        return update;
    }
}