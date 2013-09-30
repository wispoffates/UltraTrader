package com.thedemgel.cititradersre.conversation.itemadmin;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.NotADoublePrompt;
import com.thedemgel.cititradersre.shop.ItemPrice;
import java.math.BigDecimal;
import java.text.MessageFormat;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.inventory.ItemStack;

public class AdminItemSetPricePrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {

		Double price;
		try {
			price = Double.valueOf(input);
		} catch (NumberFormatException ex) {
			context.setSessionData("return", new AdminItemSetPricePrompt());
			return new NotADoublePrompt();
		}
		ItemPrice item = (ItemPrice) context.getSessionData("itemprice");
		item.setPrice(BigDecimal.valueOf(price));
		//context.setSessionData("price", BigDecimal.valueOf(price));
		return new AdminItemMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemStack item = (ItemStack) context.getSessionData("item");
		return MessageFormat.format(CitiTrader.getResourceBundle().getString("conversation.itemadmin.setprice"), item.getType().name());
	}
}
