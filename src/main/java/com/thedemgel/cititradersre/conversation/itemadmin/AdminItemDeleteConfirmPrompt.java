package com.thedemgel.cititradersre.conversation.itemadmin;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.shop.ItemPrice;
import com.thedemgel.cititradersre.shop.Shop;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AdminItemDeleteConfirmPrompt extends MessagePrompt {

	private boolean delete = false;

	public AdminItemDeleteConfirmPrompt(boolean delete) {
		this.delete = delete;
	}

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		if (delete) {
			return Prompt.END_OF_CONVERSATION;
		} else {
			return new AdminItemMenuPrompt();
		}
	}

	@Override
	public String getPromptText(ConversationContext context) {


		if (delete) {
			ShopInventoryView view = (ShopInventoryView) context.getSessionData("view");
			ItemPrice itemprice = (ItemPrice) context.getSessionData("itemprice");
			ItemStack item = (ItemStack) context.getSessionData("item");
			Shop shop = view.getShop();
			shop.getInventoryInterface().removeInventory(itemprice.getItemStack(), -1);
			String id = shop.getItemId(item);
			shop.getSellprices().remove(id);
			view.buildItemView(item);
			return CitiTrader.getResourceBundle().getString("conversation.itemadmin.delete.deleted");
		} else {
			return CitiTrader.getResourceBundle().getString("conversation.itemadmin.delete.cancelled");
		}
	}
}
