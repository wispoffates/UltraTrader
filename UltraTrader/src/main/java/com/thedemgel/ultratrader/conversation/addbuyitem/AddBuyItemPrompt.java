package com.thedemgel.ultratrader.conversation.addbuyitem;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.NotADoublePrompt;
import java.math.BigDecimal;
import java.text.MessageFormat;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.inventory.ItemStack;

public class AddBuyItemPrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {

		Double price;
		try {
			price = Double.valueOf(input);
		} catch (NumberFormatException ex) {
			context.setSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN, new AddBuyItemPrompt());
			return new NotADoublePrompt();
		}
		context.setSessionData(ConversationHandler.CONVERSATION_SESSION_PRICE, BigDecimal.valueOf(price));
		return new AddBuyItemDescriptionPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);

		return MessageFormat.format(L.getString("conversation.addbuyitem.setprice"), item.getType().name());
	}
}
