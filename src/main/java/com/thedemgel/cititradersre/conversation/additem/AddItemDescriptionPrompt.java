package com.thedemgel.cititradersre.conversation.additem;

import com.thedemgel.cititradersre.conversation.setsellprice.*;
import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.NotADoublePrompt;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import java.math.BigDecimal;
import java.text.MessageFormat;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddItemDescriptionPrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		context.setSessionData("description", input);
		return new AddItemFinishPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player player = (Player) context.getForWhom();
		ItemStack item = (ItemStack) context.getSessionData("item");
		return MessageFormat.format(CitiTrader.getResourceBundle().getString("conversation.additem.setdescription"), item.getType().name());
	}
}
