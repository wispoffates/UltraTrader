package com.thedemgel.cititradersre.conversation.admin;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.shop.ShopInventoryView;
import java.text.MessageFormat;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class AdminSetNamePrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		if (input.length() <= ConversationHandler.CONVERSATION_MAX_SHOP_NAME) {
			ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
			view.getShop().setName(input);
			return new AdminMenuPrompt();
		}
		// Will be an error message.
		return new AdminStringToLongPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return CitiTrader.getResourceBundle().getString("conversation.admin.setname.setname");
	}
}
