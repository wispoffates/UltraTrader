
package com.thedemgel.cititradersre.conversation.additem;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class AddInventoryPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		final Player player = (Player) context.getForWhom();
		final ShopInventoryView view = (ShopInventoryView) CitiTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);
		
		ItemStack item = (ItemStack) context.getSessionData("item");
		view.getShop().addInventory(item);
		view.setKeepAlive(false);
		//view.buildView();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(CitiTrader.getInstance(), new Runnable() {
			@Override
			public void run() {
				view.buildView();
				//CitiTrader.getStoreHandler().getInventoryHandler().openInventory(player);
			}
		}, 3);
		
		return CitiTrader.getResourceBundle().getString("conversation.additem.addinventory");
	}

}
