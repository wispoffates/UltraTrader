package com.thedemgel.cititradersre.conversation.additem;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.inventory.ItemStack;

public class AddItemShopFullPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		final int slot = (int) context.getSessionData("slot");
		final ItemStack item = (ItemStack) context.getSessionData("item");
		final ShopInventoryView view = (ShopInventoryView) context.getSessionData("view");

		Bukkit.getScheduler().scheduleSyncDelayedTask(CitiTrader.getInstance(), new Runnable() {
			@Override
			public void run() {
				view.setItem(slot, new ItemStack(Material.AIR));
				view.getPlayer().getInventory().addItem(item);
				//view.buildView();
				//CitiTrader.getStoreHandler().getInventoryHandler().openInventory(player);
			}
		}, 3);

		return CitiTrader.getResourceBundle().getString("conversation.additem.shopfull");
	}
}
