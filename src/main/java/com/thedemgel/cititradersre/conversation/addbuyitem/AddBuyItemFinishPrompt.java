package com.thedemgel.cititradersre.conversation.addbuyitem;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.shop.ShopInventoryView;
import java.math.BigDecimal;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.inventory.ItemStack;

public class AddBuyItemFinishPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		final ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);

		String description = (String) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_DESCRIPTION);
		final ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
		BigDecimal price = (BigDecimal) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_PRICE);

		view.getShop().addBuyItem(item, price, 1, description);
		//view.getShop().getInventoryInterface().addInventory(item);

		view.buildBuyView();

		Bukkit.getScheduler().scheduleSyncDelayedTask(CitiTrader.getInstance(), new Runnable() {
			@Override
			public void run() {
				view.getBottomInventory().addItem(item);
			}
		}, CitiTrader.BUKKIT_SCHEDULER_DELAY);

		return L.getString("conversation.addbuyitem.added");
	}
}
