package com.thedemgel.ultratrader.conversation.addsellitem;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.NotADoublePrompt;
import java.math.BigDecimal;
import java.text.MessageFormat;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.inventory.ItemStack;

public class AddItemPrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {

		Double price;
		try {
			price = Double.valueOf(input);
		} catch (NumberFormatException ex) {
			context.setSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN, new AddItemPrompt());
			return new NotADoublePrompt();
		}
		context.setSessionData(ConversationHandler.CONVERSATION_SESSION_SELLPRICE, BigDecimal.valueOf(price));
		//return new AddItemDescriptionPrompt();
        return new AddBuyItemPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);

		return MessageFormat.format(L.getString("conversation.additem.setprice"), item.getType().name());
	}
}
