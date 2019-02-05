package com.thedemgel.ultratrader.conversation.admin;

import com.thedemgel.ultratrader.Lang;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

public class AdminStringToLongPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return new AdminSetNamePrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return Lang.getString("conversation.admin.setname.nametolong");
	}
}
