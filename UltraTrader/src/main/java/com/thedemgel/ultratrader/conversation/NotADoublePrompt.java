package com.thedemgel.ultratrader.conversation;

import com.thedemgel.ultratrader.Lang;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

public class NotADoublePrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return (Prompt) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN);
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return ChatColor.RED + Lang.getString("conversation.error.notadouble");
	}
}
