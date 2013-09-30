
package com.thedemgel.cititradersre.conversation.itemadmin;

import com.thedemgel.cititradersre.conversation.admin.*;
import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.shop.ItemPrice;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import java.util.ResourceBundle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


/**
 * The first helpful message to administrate your shop.
 */
public class AdminItemBeginPrompt extends MessagePrompt {
	private ResourceBundle rb;
	private ConversationPrefix prefix;

	public AdminItemBeginPrompt() {
		prefix = new AdminConversationPrefix();
		rb = CitiTrader.getResourceBundle();
	}
	
	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		final Player player = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) CitiTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);
		
		context.setSessionData("view", view);
		
		ItemStack item = (ItemStack) context.getSessionData("item");
		String id = view.getShop().getItemId(item);
		ItemPrice itemprice = view.getShop().getSellprices().get(id);
		
		context.setSessionData("itemprice", itemprice);
		
		view.setKeepAlive(true);
		Bukkit.getScheduler().scheduleSyncDelayedTask(CitiTrader.getInstance(), new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
			}
		}, 3);
		
		return new AdminItemMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player p = (Player) context.getForWhom();
		p.sendRawMessage(prefix.getPrefix(context) + rb.getString("conversation.itemadmin.begin"));
		return rb.getString("conversation.toquit");
	}

}
