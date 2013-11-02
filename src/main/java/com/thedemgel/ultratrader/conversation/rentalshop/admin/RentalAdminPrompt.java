
package com.thedemgel.ultratrader.conversation.rentalshop.admin;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;


public class RentalAdminPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext cc) {
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext cc) {
		return "Admin section";
	}

}
