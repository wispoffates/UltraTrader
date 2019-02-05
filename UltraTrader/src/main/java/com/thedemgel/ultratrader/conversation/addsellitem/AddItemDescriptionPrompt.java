package com.thedemgel.ultratrader.conversation.addsellitem;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.inventory.ItemStack;

public class AddItemDescriptionPrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		if (input.equalsIgnoreCase(Lang.getString("conversation.additem.none"))) {
			context.setSessionData(ConversationHandler.CONVERSATION_SESSION_DESCRIPTION, "");
		} else {
			context.setSessionData(ConversationHandler.CONVERSATION_SESSION_DESCRIPTION, input);
		}
		return new AddItemFinishPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
		return Lang.getFormatString("conversation.additem.setdescription", item.getType().name(), Lang.getString("conversation.additem.none"));
	}
}
