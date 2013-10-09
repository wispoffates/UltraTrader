package com.thedemgel.ultratrader.conversation.createshop;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

public class CreateShopAssignPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return new CreateShopMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return "This does nothing yet";
	}
}
