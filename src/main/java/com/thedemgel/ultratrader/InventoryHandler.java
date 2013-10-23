package com.thedemgel.ultratrader;

import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.shop.ShopInventoryView;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

/**
 * Create and store temporary Inventories for all stores/players Each time a
 * player opens an inventory from a store they will generate a new Inventory,
 * when that inventory is closed, anything that is purchased will be double
 * checked against what the NPC has, and then all items will attempted to be put
 * into the players inventory.
 */
public class InventoryHandler {
	public static final int INVENTORY_SIZE = 54;
	public static final int INVENTORY_ADMIN_SLOT = 53;
	public static final int INVENTORY_TAKE_ALL_SLOT = 50;
	public static final int INVENTORY_BACK_ARROW_SLOT = 45;
	public static final int INVENTORY_CREATE_ITEM_SLOT = 51;
	public static final int MAX_SELL_BUY_ITEMS = 36;

	private Map<Player, ShopInventoryView> inventories = new ConcurrentHashMap<>();
	private final UltraTrader plugin;

	public InventoryHandler(UltraTrader instance) {
		plugin = instance;
	}

	public void openInventory(Player player) {
		if (inventories.containsKey(player)) {
			ShopInventoryView inv = inventories.get(player);
			if (inv.getConvo() != null) {
				inv.getConvo().abandon();
				inv.setConvo(null);
			}
			player.openInventory(inv);
		}
	}

	public final void createBuyInventoryView(Player player, Shop shop) {
		// Clear any old InventoryViews if they exist
		if (inventories.containsKey(player)) {
			ShopInventoryView oldview = inventories.get(player);
			if (oldview.getConvo() != null) {
				oldview.getConvo().abandon();
				inventories.remove(player);
			}
		}

		Inventory inv = plugin.getServer().createInventory(null, INVENTORY_SIZE, shop.getName());
		ShopInventoryView view = new ShopInventoryView(inv, player, shop);
		if (inventories.containsKey(player)) {
			closeInventoryView(player);
			removeInventoryView(player);
		}

		inventories.put(player, view);
	}

	public final void updateInventory(Player player, Shop shop) {
	}

	public final void closeInventoryView(Player player) {
		player.closeInventory();
	}

	public final void removeInventoryView(Player player) {
		if (inventories.containsKey(player)) {
			inventories.remove(player);
		}
	}

	public final InventoryView getInventoryView(Player player) {
		if (inventories.containsKey(player)) {
			return inventories.get(player);
		}

		return null;
	}

	public final boolean hasInventoryView(Player player) {
		return inventories.containsKey(player);
	}
}
