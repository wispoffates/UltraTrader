package com.thedemgel.cititradersre;

import com.thedemgel.cititradersre.citizens.TraderTrait;
import com.thedemgel.cititradersre.util.Permissions;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import com.thedemgel.cititradersre.util.Status;
import java.util.List;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopListener implements Listener {

	private final CitiTrader plugin;

	public ShopListener(CitiTrader instance) {
		plugin = instance;
	}

	@EventHandler
	public void onInventoryClickEvent(final InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		if (event.getView() instanceof ShopInventoryView) {
			final ShopInventoryView view = (ShopInventoryView) event.getView();

			// Nothing needs to be dragged anymore...
			if (event.getRawSlot() < view.getTopInventory().getSize()) {
				event.setCancelled(true);
			} else {
				return;
			}

			if (view.getStatus().equals(Status.MAIN_SCREEN)) {
				
				if (event.getCurrentItem() != null) {
					view.buildItemView(event.getCurrentItem());
				}
			} else if (view.getStatus().equals(Status.SELL_SCREEN)) {
				if (event.getRawSlot() == 45) {
					view.buildView();
				}

				if (event.getRawSlot() == 53) {
					Conversation convo = CitiTrader.getConversationHandler().getSetSellPrice().buildConversation(player);
					convo.getContext().setSessionData("item", event.getCurrentItem());
					convo.begin();
					return;
				}

				if (event.getCurrentItem() != null) {
					PurchaseHandler.processPurchase(view.getShop(), (Player) event.getWhoClicked(), event.getCurrentItem());
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							view.buildItemView(event.getCurrentItem());
						}
					}, 2);
				}
			}
		}
	}

	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		if (event.getView() instanceof ShopInventoryView) {
			ShopInventoryView view = (ShopInventoryView) event.getView();
			if (!view.isKeepAlive()) {
				CitiTrader.getStoreHandler().getInventoryHandler().removeInventoryView((Player) event.getPlayer());
			}
		}
	}

	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		Entity entity = event.getRightClicked();
		Player player = event.getPlayer();

		// Check if player can even access stores
		if (!player.hasPermission(Permissions.USE_STORES)) {
			return;
		}

		if (plugin.isCitizens()) {
			if (!CitizensAPI.getNPCRegistry().isNPC(entity)) {
				return;
			}

			NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);

			if (npc.hasTrait(TraderTrait.class)) {
				TraderTrait trait = npc.getTrait(TraderTrait.class);

				Integer shopid = trait.getShopId();

				if (shopid == -1) {
					player.sendMessage("Trader is not open (no shop assigned)");
					return;
				}

				// Open Store
				CitiTrader.getStoreHandler().getInventoryHandler().createBuyInventoryView(player, plugin.getStoreHandler().getShop(shopid));
				CitiTrader.getStoreHandler().getInventoryHandler().openInventory(player);
			}
		}
	}

	/**
	 * Checks when a player interacts with a block or item (in hand) to see
	 * if it is a store.
	 *
	 * @param event
	 */
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		// Check if player can even access stores
		if (!player.hasPermission(Permissions.USE_STORES)) {
			return;
		}

		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Block block = event.getClickedBlock();
			if (block.hasMetadata("shop")) {
				return;
			}
		}

		if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			ItemStack item = event.getItem();

			// after here is all itemsinhand -- if no item in hand. return.
			if (item == null) {
				return;
			}

			if (!item.hasItemMeta()) {
				return;
			}

			switch (item.getItemMeta().getDisplayName()) {
				case "Store":
					// Open Store
					CitiTrader.getStoreHandler().getInventoryHandler().createBuyInventoryView(player, plugin.getStoreHandler().getShop(1));
					CitiTrader.getStoreHandler().getInventoryHandler().openInventory(player);
					break;
				case "Create Shop":
					createShop(player, item, event.getClickedBlock());
					break;
			}
		}
	}

	public void createShop(Player player, ItemStack item, Block block) {
		if (!player.hasPermission(Permissions.CREATE_STORES)) {
			player.sendMessage("You do not have permission to open a shop.");
			return;
		}
		if (item.getItemMeta().hasLore()) {
			ItemMeta meta = item.getItemMeta();
			Integer uses = Integer.valueOf(meta.getLore().get(2).split(" ")[0]) - 1;
			List<String> metas = meta.getLore();
			metas.set(2, String.valueOf(uses) + " uses left.");
			meta.setLore(metas);
			item.setItemMeta(meta);
		}
	}
}
