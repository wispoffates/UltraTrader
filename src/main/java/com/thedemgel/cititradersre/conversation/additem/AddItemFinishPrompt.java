package com.thedemgel.cititradersre.conversation.additem;

import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.shop.ShopInventoryView;
import java.math.BigDecimal;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.inventory.ItemStack;

public class AddItemFinishPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		
		String description = (String) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_DESCRIPTION);
		ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
		BigDecimal price = (BigDecimal) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_PRICE);

		view.getShop().addSellItem(item, price, 1, description);
		view.getShop().getInventoryInterface().addInventory(item);

		view.buildView();

		return L.getString("conversation.additem.added");
	}
}
