package com.thedemgel.ultratrader.conversation.addsellitem;

import com.thedemgel.ultratrader.L;
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
			context.setSessionData("return", new AddItemPrompt());
			return new NotADoublePrompt();
		}
		context.setSessionData("price", BigDecimal.valueOf(price));
		return new AddItemDescriptionPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemStack item = (ItemStack) context.getSessionData("item");
		
		return MessageFormat.format(L.getString("conversation.additem.setprice"), item.getType().name());
	}
}
