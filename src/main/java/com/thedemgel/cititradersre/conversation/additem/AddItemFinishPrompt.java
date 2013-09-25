
package com.thedemgel.cititradersre.conversation.additem;

import com.thedemgel.cititradersre.conversation.setsellprice.*;
import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import java.math.BigDecimal;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class AddItemFinishPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		final Player player = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) CitiTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);
		view.setKeepAlive(false);
		
		//ItemStack item = (ItemStack) context.getSessionData("item");
		//String id = view.getShop().getItemId(item);
		//BigDecimal price = (BigDecimal) context.getSessionData("price");
		//view.getShop().getSellprices().get(id).setPrice(price);
		view.buildView();
		Bukkit.getScheduler().scheduleSyncDelayedTask(CitiTrader.getInstance(), new Runnable() {
			@Override
			public void run() {
				CitiTrader.getStoreHandler().getInventoryHandler().openInventory(player);
			}
		}, 3);
		
		return CitiTrader.getResourceBundle().getString("conversation.additem.added");
	}

}
