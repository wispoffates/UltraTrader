package com.thedemgel.ultratrader.conversation.buyitemadmin;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.NotADoublePrompt;
import com.thedemgel.ultratrader.shop.ItemPrice;
import java.math.BigDecimal;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class AdminBuyItemSetPricePrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {

		Double price;
		try {
			price = Double.valueOf(input);
		} catch (NumberFormatException ex) {
			context.setSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN, new AdminBuyItemSetPricePrompt());
			return new NotADoublePrompt();
		}
		ItemPrice item = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		item.setBuyPrice(BigDecimal.valueOf(price));
		return new AdminBuyItemMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemPrice item = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		return Lang.getFormatString("conversation.itemadmin.setprice", item.getItemStack().getType().name());
	}
}
