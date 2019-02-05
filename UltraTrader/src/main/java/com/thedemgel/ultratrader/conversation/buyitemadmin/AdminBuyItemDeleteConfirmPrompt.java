package com.thedemgel.ultratrader.conversation.buyitemadmin;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.inventory.ItemStack;

public class AdminBuyItemDeleteConfirmPrompt extends MessagePrompt {

	private boolean delete = false;

	public AdminBuyItemDeleteConfirmPrompt(boolean delete) {
		this.delete = delete;
	}

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		if (delete) {
			return Prompt.END_OF_CONVERSATION;
		} else {
			return new AdminBuyItemMenuPrompt();
		}
	}

	@Override
	public String getPromptText(ConversationContext context) {


		if (delete) {
			ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
			ItemPrice itemprice = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
			ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
			Shop shop = view.getShop();
			if (!shop.hasItem(itemprice)) {
				shop.getInventoryInterface().removeInventory(itemprice.getItemStack(), -1);
			}
			String id = shop.getItemId(item);
			shop.getPriceList().remove(id);
			view.buildBuyItemView(item);
			return Lang.getString("conversation.itemadmin.delete.deleted");
		} else {
			return Lang.getString("conversation.itemadmin.delete.cancelled");
		}
	}
}
