package com.springboot.MyTodoList.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotHelper {

	private static final int MAX_MESSAGE_LENGTH = 4000;
	private static final Logger logger = LoggerFactory.getLogger(BotHelper.class);

	public static void executeMessage(Long chatId, SendMessage messageToTelegram, TelegramLongPollingBot bot) {
		String messageText = messageToTelegram.getText();

		if (messageText.length() <= MAX_MESSAGE_LENGTH) {
			// If the message is short enough, send it normally
			try {
				messageToTelegram.setChatId(chatId.toString());
				bot.execute(messageToTelegram);
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		} else {
			// if the message is too long, split it into chunks
			int totalChunks = (int) Math.ceil((double) messageText.length() / MAX_MESSAGE_LENGTH);
			for (int i = 0; i < totalChunks; i++) {
				int startIndex = i * MAX_MESSAGE_LENGTH;
				int endIndex = Math.min((i + 1) * MAX_MESSAGE_LENGTH, messageText.length());
				String chunk = messageText.substring(startIndex, endIndex);

				// Add chunk indicator if splitting into multiple messages
				if (totalChunks > 1) {
					chunk = "Part " + (i + 1) + "/" + totalChunks + ":\n\n" + chunk;
				}

				SendMessage chunckedMessage = new SendMessage();
				chunckedMessage.setChatId(chatId);
				chunckedMessage.setText(chunk);

				// Only add the reply markup to the last chunk
				ReplyKeyboard replyMarkup = messageToTelegram.getReplyMarkup();
				if (i == totalChunks - 1 && replyMarkup != null) {
					chunckedMessage.setReplyMarkup(replyMarkup);
				}

				try {
					bot.execute(chunckedMessage);
				} catch (TelegramApiException e) {
					logger.error("Error sending chunked message: " + e.getMessage(), e);
				}
			}
		}
	}
}
