package com.thedemgel.cititradersre;

import com.thedemgel.cititradersre.util.Permissions;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import com.thedemgel.cititradersre.util.Status;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopListener implements Listener {

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		
		if (event.getView() instanceof ShopInventoryView) {
			ShopInventoryView view = (ShopInventoryView) event.getView();
			System.out.println(event.getRawSlot());
			// Nothing needs to be dragged anymore...
			event.setCancelled(true);
			if (view.getStatus().equals(Status.MAIN_SCREEN)) {
				view.buildItemView(event.getCurrentItem());
			} else if (view.getStatus().equals(Status.SELL_SCREEN)) {
				if (event.getRawSlot() == 45) {
					view.buildView();
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
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
		Block block = event.getClickedBlock();

		if (block.hasMetadata("shop")) {
			return;
		}

		ItemStack item = event.getItem();

		if (!item.hasItemMeta()) {
			return;
		}

		switch (item.getItemMeta().getDisplayName()) {
			case "Shop":
				// Open Store
				break;
			case "Create Shop":
				createShop(player, item, block);
				break;
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
