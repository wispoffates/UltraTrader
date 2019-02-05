package com.thedemgel.ultratrader.conversation.admin;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;

import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * The first helpful message to administrate your shop.
 */
public class AdminBeginPrompt extends MessagePrompt {

	private ConversationPrefix prefix;

	public AdminBeginPrompt() {
		prefix = new AdminConversationPrefix();
	}

	@Override
	protected final Prompt getNextPrompt(ConversationContext context) {
		final Player player = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) UltraTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);

		context.setSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW, view);

		view.setKeepAlive(true);
		Bukkit.getScheduler().scheduleSyncDelayedTask(UltraTrader.getInstance(), new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
			}
		}, UltraTrader.BUKKIT_SCHEDULER_DELAY);

		return new AdminMenuPrompt();
	}

	@Override
	public final String getPromptText(ConversationContext context) {
		Player p = (Player) context.getForWhom();
		p.sendRawMessage(prefix.getPrefix(context) + Lang.getString("conversation.admin.begin"));
		return Lang.getString("conversation.toquit");
	}
}
