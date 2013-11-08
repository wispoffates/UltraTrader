package com.thedemgel.ultratrader.conversation.addbuyitem;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.inventory.ItemStack;

public class AddBuyItemDescriptionPrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		if (input.equalsIgnoreCase(L.getString("conversation.addbuyitem.none"))) {
			context.setSessionData(ConversationHandler.CONVERSATION_SESSION_DESCRIPTION, "");
		} else {
			context.setSessionData(ConversationHandler.CONVERSATION_SESSION_DESCRIPTION, input);
		}
		return new AddBuyItemFinishPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
		return L.getFormatString("conversation.addbuyitem.setdescription", item.getType().name(), L.getString("conversation.addbuyitem.none"));
	}
}
