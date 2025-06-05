package com.springboot.MyTodoList.util;

public enum BotMessages {
	
	HELLO_MYTODO_BOT(
	"Hello! I'm MyTodoList Bot!\nType a new todo item below and press the send button (blue arrow), or select an option below:"),
	BOT_REGISTERED_STARTED("Bot registered and started succesfully!"),
	ITEM_DONE("Item done! Select /todolist to return to the list of todo items, or /start to go to the main screen."), 
	ITEM_UNDONE("Item undone! Select /todolist to return to the list of todo items, or /start to go to the main screen."), 
	ITEM_DELETED("Item deleted! Select /todolist to return to the list of todo items, or /start to go to the main screen."),
	ITEM_UPDATED("Item updated! Select /todolist to return to the list of todo items, or /start to go to the main screen."),
	TYPE_NEW_TODO_ITEM("Type a new todo item below in the format: Title, Description, DD-MM-YYYY, State, Sprint, User, StoryPoints, Priority, EstimatedHours, RealHours. Leave blank spaces for the fields you don't want to set."),
	TYPE_UPDATE_TODO_ITEM("Type the updated fields of the todo item below in the format: Title, Description, DD-MM-YYYY, State, Sprint, User, StoryPoints, Priority, EstimatedHours, RealHours. Leave blank spaces for the fields you don't want to update."),
	NEW_ITEM_ADDED("New item added! Select /todolist to return to the list of todo items, or /start to go to the main screen."),
	BYE("Bye! Select /start to resume!"),
	ERROR("Error, sorry! this bot is in development"),

	LOGIN("Please login to use the bot. Type your phone number and password in the format: PhoneNumber, Password, Remember Me? [0/1]."),
	REMEMBER("Would you like me to remember your phone number and password? [yes/no]"),
	LOGIN_SUCCESS("Login successful! Select /todolist to return to the list of todo items, or /start to go to the main screen."),
	LOGIN_FAILED("Login failed! Please try again."),
	LOGOUT_SUCCESS("You have been logged out! Select /start to go to the main screen."),
	LOGOUT_FAILED("Logout failed! Please try again."),

	TYPE_USER_KPI("Type the name of the user you want to see this sprint's KPI.");

	private String message;

	BotMessages(String enumMessage) {
		this.message = enumMessage;
	}

	public String getMessage() {
		return message;
	}

}
