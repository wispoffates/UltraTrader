
package com.thedemgel.cititradersre.shop;

import com.google.common.collect.Iterables;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;


public class Shop {
	private ConcurrentMap<String, InventoryItem> inventory = new ConcurrentHashMap<>();
	private ConfigurationSection config;
	
	public Shop(ConfigurationSection section) {
		
		config = section;
		
		addItem(new ItemStack(Material.ANVIL), 3D, 1, "Some Lore Text");
		addItem(new ItemStack(Material.BAKED_POTATO), 3D, 2, "Some Lore Text");
		addItem(new ItemStack(Material.BED), 3D, 3, "");
		addItem(new ItemStack(Material.BED_BLOCK), 3D, 4, "Some Lore Text");
		addItem(new ItemStack(Material.BLAZE_POWDER), 3D, 8, "Sizzling Hot");
		addItem(new ItemStack(Material.BOAT), 3D, 12, "Some Lore Text");
		addItem(new ItemStack(Material.BOOK), 3D, 16, "");
		addItem(new ItemStack(Material.BOOK_AND_QUILL), 3D, 30, "Need to remember something?");
		addItem(new ItemStack(Material.BURNING_FURNACE), 3D, 32, "Some Lore Text");
		addItem(new ItemStack(Material.CARPET), 3D, 54, "Soft floor covering.");
		addItem(new ItemStack(Material.BUCKET), 3D, 64, "Soft floor covering.");
		addItem(new ItemStack(Material.BONE), 3D, 128, "Soft floor covering.");
		
		setMetaData();
	}
	
	public ConcurrentMap<String, InventoryItem> getInventory() {
		return inventory;
	}
	
	public void addItem(ItemStack item, Double price, Integer quantity, String description) {
		InventoryItem invItem = new InventoryItem(item, price, quantity, description);
		String random = invItem.setRandom();
		while (inventory.containsKey(random)) {
			random = invItem.setRandom();
		}
		inventory.put(random, invItem);
	}
	
	private void setMetaData() {
		// Decide whether to set metadata (not if item store) and then set it.
	}
	
	public String getItemId(ItemStack item) {
		String id = Iterables.getLast(item.getItemMeta().getLore());
		id = id.substring(id.length() - 8);
		return id;
	}
}
