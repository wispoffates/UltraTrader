package com.thedemgel.ultratrader.conversation.createshop;

import com.thedemgel.ultratrader.Lang;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

public class CreateShopBeginPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return new CreateShopMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return Lang.getString("conversation.createshop.begin");
	}
}
