package com.thedemgel.cititradersre.conversation.sellitemadmin;

import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.shop.ItemPrice;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class AdminSellItemDescriptionPrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		ItemPrice itemprice = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		if (input.equalsIgnoreCase(L.getString("conversation.itemadmin.none"))) {
			itemprice.setDescription("");
		} else {
			itemprice.setDescription(input);
		}
		return new AdminSellItemMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemPrice item = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		return L.getFormatString("conversation.itemadmin.setdescription", item.getItemStack().getType().name(), L.getString("conversation.itemadmin.none"));
	}
}
