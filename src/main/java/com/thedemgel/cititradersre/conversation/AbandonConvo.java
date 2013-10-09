package com.thedemgel.cititradersre.conversation;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.shop.ShopInventoryView;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public class AbandonConvo implements ConversationAbandonedListener {

	@Override
	public final void conversationAbandoned(ConversationAbandonedEvent abandonedEvent) {
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
					}, CitiTrader.BUKKIT_SCHEDULER_DELAY);
				}
			}
		} else {
			final Player player = (Player) abandonedEvent.getContext().getForWhom();
			if (CitiTrader.getStoreHandler().getInventoryHandler().hasInventoryView(player)) {
				final ShopInventoryView view = (ShopInventoryView) CitiTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);

				view.setKeepAlive(false);

				Bukkit.getScheduler().scheduleSyncDelayedTask(CitiTrader.getInstance(), new Runnable() {
					@Override
					public void run() {
						InventoryView playerview = player.getOpenInventory();
						if (!playerview.equals(view)) {
							CitiTrader.getStoreHandler().getInventoryHandler().removeInventoryView(player);
						}
					}
				}, CitiTrader.BUKKIT_SCHEDULER_DELAY);

			}
			abandonedEvent.getContext().getForWhom().sendRawMessage(L.getString("conversation.abandon"));
		}
	}
}
