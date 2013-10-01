package com.thedemgel.cititradersre.conversation;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.entity.Player;

public class AbandonConvo implements ConversationAbandonedListener {

	@Override
	public void conversationAbandoned(ConversationAbandonedEvent abandonedEvent) {
		if (abandonedEvent.gracefulExit()) {
			final Player player = (Player) abandonedEvent.getContext().getForWhom();
			if (CitiTrader.getStoreHandler().getInventoryHandler().hasInventoryView(player)) {
				ShopInventoryView view = (ShopInventoryView) CitiTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);
				view.getShop().save();
				if (view.isKeepAlive()) {
					view.setKeepAlive(false);

					Bukkit.getScheduler().scheduleSyncDelayedTask(CitiTrader.getInstance(), new Runnable() {
						@Override
						public void run() {
							CitiTrader.getStoreHandler().getInventoryHandler().openInventory(player);
						}
					}, 3);
				}
			}
		} else {
			final Player player = (Player) abandonedEvent.getContext().getForWhom();
			if (CitiTrader.getStoreHandler().getInventoryHandler().hasInventoryView(player)) {
				ShopInventoryView view = (ShopInventoryView) CitiTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);

				view.setKeepAlive(false);
				CitiTrader.getStoreHandler().getInventoryHandler().removeInventoryView(player);
			}
			abandonedEvent.getContext().getForWhom().sendRawMessage(CitiTrader.getResourceBundle().getString("conversation.abandon"));
		}
	}
}
