package com.thedemgel.ultratrader.conversation.buyitemadmin;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.shop.ItemPrice;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class AdminBuyItemDescriptionPrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		ItemPrice itemprice = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		if (input.equalsIgnoreCase(Lang.getString("conversation.itemadmin.none"))) {
			itemprice.setDescription("");
		} else {
			itemprice.setDescription(input);
		}
		return new AdminBuyItemMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemPrice item = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		return Lang.getFormatString("conversation.itemadmin.setdescription", item.getItemStack().getType().name(), Lang.getString("conversation.itemadmin.none"));
	}
}
