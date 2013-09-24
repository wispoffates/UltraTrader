
package com.thedemgel.cititradersre;

import com.thedemgel.cititradersre.shop.Shop;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;


/**
 * Create and store temporary Inventories for all stores/players
 * Each time a player opens an inventory from a store they will
 * generate a new Inventory, when that inventory is closed, anything that is
 * purchased will be double checked against what the NPC has, and
 * then all items will attempted to be put into the players inventory.
 */
public class InventoryHandler {
	private Map<Player, ShopInventoryView> inventories = new ConcurrentHashMap<>();
	
	private final CitiTrader plugin;
	
	public InventoryHandler(CitiTrader instance) {
		plugin = instance;
	}
	
	public void openInventory(Player player) {
		if (inventories.containsKey(player)) {
			InventoryView inv = inventories.get(player);
			player.openInventory(inv);
		}
	}
	
	public void createBuyInventoryView(Player player, Shop shop) {
		Inventory inv = plugin.getServer().createInventory(null, 54, "Eventually shop name");
		Inventory bottom = plugin.getServer().createInventory(null, 36, "test");
		ShopInventoryView view = new ShopInventoryView(inv, bottom, player, shop);
		if (inventories.containsKey(player)) {
			closeInventoryView(player);
			removeInventoryView(player);
		}
		
		inventories.put(player, view);
	}
	
	public void updateInventory(Player player, Shop shop) {
		
	}
	
	public void closeInventoryView(Player player) {
		player.closeInventory();
	}
	
	public void removeInventoryView(Player player) {
		if (inventories.containsKey(player)) {
			inventories.remove(player);
		}
	}
	
	public InventoryView getInventoryView(Player player) {
		if (inventories.containsKey(player)) {
			return inventories.get(player);
		}
		
		return null;
	}
}
