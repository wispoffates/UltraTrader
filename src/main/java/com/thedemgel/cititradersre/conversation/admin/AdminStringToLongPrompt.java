
package com.thedemgel.cititradersre.conversation.admin;

import com.thedemgel.cititradersre.CitiTrader;
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
		return CitiTrader.getResourceBundle().getString("conversation.admin.setname.nametolong");
	}

}
