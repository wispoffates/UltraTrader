package com.thedemgel.ultratrader.conversation.buyitemadmin;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The first helpful message to administrate your shop.
 */
public class AdminBuyItemBeginPrompt extends MessagePrompt {

	private ConversationPrefix prefix;

	public AdminBuyItemBeginPrompt() {
		prefix = new AdminBuyItemConversationPrefix();
	}

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		final Player player = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) UltraTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);

		context.setSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW, view);

		ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
		String id = view.getShop().getItemId(item);
		ItemPrice itemprice = view.getShop().getPriceList().get(id);

		context.setSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE, itemprice);

		view.setKeepAlive(true);
		Bukkit.getScheduler().scheduleSyncDelayedTask(UltraTrader.getInstance(), new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
			}
		}, UltraTrader.BUKKIT_SCHEDULER_DELAY);

		return new AdminBuyItemMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player p = (Player) context.getForWhom();
		p.sendRawMessage(prefix.getPrefix(context) + Lang.getString("conversation.itemadmin.begin"));
		return Lang.getString("conversation.toquit");
	}
}
