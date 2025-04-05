package com.springboot.MyTodoList.util;

public enum BotCommands {

	START_COMMAND("/start"), 
	HIDE_COMMAND("/hide"), 
	TODO_LIST("/todolist"),
	ADD_ITEM("/additem"),
	UPDATE_ITEM("/updateitem");

	private String command;

	BotCommands(String enumCommand) {
		this.command = enumCommand;
	}

	public String getCommand() {
		return command;
	}
}
