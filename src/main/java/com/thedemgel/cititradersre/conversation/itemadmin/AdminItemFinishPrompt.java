package com.thedemgel.cititradersre.conversation.itemadmin;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AdminItemFinishPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		final Player player = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) CitiTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);
		ItemStack item = (ItemStack) context.getSessionData("item");
		view.buildItemView(item);

		return CitiTrader.getResourceBundle().getString("conversation.admin.exit");
	}
}
