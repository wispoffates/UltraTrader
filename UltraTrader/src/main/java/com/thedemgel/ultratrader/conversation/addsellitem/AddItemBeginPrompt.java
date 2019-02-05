
package com.thedemgel.ultratrader.conversation.addsellitem;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.LimitHandler;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import com.thedemgel.ultratrader.shop.ItemPrice;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Prompt beginning add item to sell in a shop
 */
public class AddItemBeginPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		final Player player = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) UltraTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);
        //ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		context.setSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW, view);
		ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
		ItemPrice itemPrice = new ItemPrice(item);
		if (view.getShop().hasItem(itemPrice)) {
			return new AddInventoryPrompt();
		}

        String category = view.getCategory();

		if (view.getShop().getItemsInCategory(category).size() >= LimitHandler.getMaxBuySellSize(view.getShop())) {
			return new AddItemShopFullPrompt();
		}

		view.setKeepAlive(true);
		Bukkit.getScheduler().scheduleSyncDelayedTask(UltraTrader.getInstance(), new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
			}
		}, UltraTrader.BUKKIT_SCHEDULER_DELAY);

		return new AddItemPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return Lang.getString("conversation.additem.begin");
	}

}
