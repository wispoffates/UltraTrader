package com.thedemgel.ultratrader.conversation.addbuyitem;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.inventory.ItemStack;

public class AddBuyItemCantAddInventoryPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		final ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		//Player player = (Player) context.getForWhom();
		final ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
		//view.getShop().getInventoryInterface().addInventory(item);
		view.setKeepAlive(false);

		Bukkit.getScheduler().scheduleSyncDelayedTask(UltraTrader.getInstance(), new Runnable() {
			@Override
			public void run() {
				view.getBottomInventory().addItem(item);
				view.buildCategoryView();
			}
		}, UltraTrader.BUKKIT_SCHEDULER_DELAY);

		return Lang.getString("conversation.addbuyitem.cantaddinventory");
	}
}
