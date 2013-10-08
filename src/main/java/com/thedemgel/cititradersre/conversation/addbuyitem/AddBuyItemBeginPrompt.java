
package com.thedemgel.cititradersre.conversation.addbuyitem;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.InventoryHandler;
import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.shop.ItemPrice;
import com.thedemgel.cititradersre.shop.ShopInventoryView;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class AddBuyItemBeginPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		final Player player = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) CitiTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);
		context.setSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW, view);
		ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
		ItemPrice itemprice = new ItemPrice(item);
		if (view.getShop().hasBuyItem(itemprice)) {
			return new AddBuyItemCantAddInventoryPrompt();
		}

		if (view.getShop().getBuyprices().size() >= InventoryHandler.MAX_SELL_BUY_ITEMS) {
			return new AddItemShopFullPrompt();
		}

		view.setKeepAlive(true);
		Bukkit.getScheduler().scheduleSyncDelayedTask(CitiTrader.getInstance(), new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
			}
		}, CitiTrader.BUKKIT_SCHEDULER_DELAY);

		return new AddBuyItemPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return L.getString("conversation.addbuyitem.begin");
	}

}
