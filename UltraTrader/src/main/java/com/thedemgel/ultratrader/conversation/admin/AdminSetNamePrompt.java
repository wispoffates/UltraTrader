package com.thedemgel.ultratrader.conversation.admin;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

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
		return Lang.getString("conversation.admin.setname.setname");
	}
}
