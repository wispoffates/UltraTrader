package com.thedemgel.cititradersre.conversation.itemadmin;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.shop.ItemPrice;
import java.text.MessageFormat;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.inventory.ItemStack;

public class AdminItemDescriptionPrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		ItemPrice itemprice = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		if (input.equalsIgnoreCase(CitiTrader.getResourceBundle().getString("conversation.itemadmin.none"))) {
			itemprice.setDescription("");
		} else {
			itemprice.setDescription(input);
		}
		return new AdminItemMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
		return MessageFormat.format(CitiTrader.getResourceBundle().getString("conversation.itemadmin.setdescription"), item.getType().name(), CitiTrader.getResourceBundle().getString("conversation.itemadmin.none"));
	}
}
