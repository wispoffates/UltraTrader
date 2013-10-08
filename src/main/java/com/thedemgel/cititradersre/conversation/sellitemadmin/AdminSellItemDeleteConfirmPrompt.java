package com.thedemgel.cititradersre.conversation.sellitemadmin;

import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.shop.ItemPrice;
import com.thedemgel.cititradersre.shop.Shop;
import com.thedemgel.cititradersre.shop.ShopInventoryView;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.inventory.ItemStack;

public class AdminSellItemDeleteConfirmPrompt extends MessagePrompt {

	private boolean delete = false;

	public AdminSellItemDeleteConfirmPrompt(boolean delete) {
		this.delete = delete;
	}

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		if (delete) {
			return Prompt.END_OF_CONVERSATION;
		} else {
			return new AdminSellItemMenuPrompt();
		}
	}

	@Override
	public String getPromptText(ConversationContext context) {


		if (delete) {
			ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
			ItemPrice itemprice = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
			ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
			Shop shop = view.getShop();
			if (!shop.hasBuyItem(itemprice)) {
				shop.getInventoryInterface().removeInventory(itemprice.getItemStack(), -1);
			}
			String id = shop.getItemId(item);
			shop.getSellprices().remove(id);
			view.buildItemView(item);
			return L.getString("conversation.itemadmin.delete.deleted");
		} else {
			return L.getString("conversation.itemadmin.delete.cancelled");
		}
	}
}
