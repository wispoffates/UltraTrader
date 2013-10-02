
package com.thedemgel.cititradersre.conversation.additem;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.shop.ItemPrice;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class AddItemBeginPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		final Player player = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) CitiTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);
		context.setSessionData("view", view);
		ItemStack item = (ItemStack) context.getSessionData("item");
		ItemPrice itemprice = new ItemPrice(item);
		if (view.getShop().hasSellItem(itemprice)) {
			return new AddInventoryPrompt();
		}
		
		if (view.getShop().getInventory().size() >= 36) {
			return new AddItemShopFullPrompt();
		}
		
		view.setKeepAlive(true);
		Bukkit.getScheduler().scheduleSyncDelayedTask(CitiTrader.getInstance(), new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
			}
		}, 3);
		
		return new AddItemPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {		
		return CitiTrader.getResourceBundle().getString("conversation.additem.begin");
	}

}
