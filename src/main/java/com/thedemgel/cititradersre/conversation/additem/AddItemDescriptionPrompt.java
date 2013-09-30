package com.thedemgel.cititradersre.conversation.additem;

import com.thedemgel.cititradersre.CitiTrader;
import java.text.MessageFormat;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.inventory.ItemStack;

public class AddItemDescriptionPrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		if (input.equalsIgnoreCase(CitiTrader.getResourceBundle().getString("conversation.additem.none"))) {
			context.setSessionData("description", "");
		} else {
			context.setSessionData("description", input);
		}
		return new AddItemFinishPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemStack item = (ItemStack) context.getSessionData("item");
		return MessageFormat.format(CitiTrader.getResourceBundle().getString("conversation.additem.setdescription"), item.getType().name(), CitiTrader.getResourceBundle().getString("conversation.additem.none"));
	}
}
