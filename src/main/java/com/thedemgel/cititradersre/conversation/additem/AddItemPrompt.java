package com.thedemgel.cititradersre.conversation.additem;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.NotADoublePrompt;
import com.thedemgel.cititradersre.shop.ShopInventoryView;
import java.math.BigDecimal;
import java.text.MessageFormat;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
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
		
		return MessageFormat.format(CitiTrader.getResourceBundle().getString("conversation.additem.setprice"), item.getType().name());
	}
}
