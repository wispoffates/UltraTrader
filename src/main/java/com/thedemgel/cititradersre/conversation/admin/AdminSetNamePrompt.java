package com.thedemgel.cititradersre.conversation.admin;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import java.text.MessageFormat;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class AdminSetNamePrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		if (input.length() <= 32) {
			Player player = (Player) context.getForWhom();
			ShopInventoryView view = (ShopInventoryView) CitiTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);
			view.getShop().setName(input);
			return new AdminMenuPrompt();
		}
		// Will be an error message.
		return new AdminStringToLongPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return CitiTrader.getResourceBundle().getString("conversation.admin.setname.setname");
	}
}
