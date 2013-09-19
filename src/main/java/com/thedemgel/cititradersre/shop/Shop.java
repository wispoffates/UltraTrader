
package com.thedemgel.cititradersre.shop;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class Shop {
	private List<InventoryItem> inventory = new ArrayList<>();
	
	public Shop() {
		inventory.add(new InventoryItem(new ItemStack(Material.ANVIL, 1), 3D, 30, "Some Lore Text"));
		inventory.add(new InventoryItem(new ItemStack(Material.BAKED_POTATO, 1), 3D, 30, "Some Lore Text"));
		inventory.add(new InventoryItem(new ItemStack(Material.BED, 1), 3D, 30, "Some Lore Text"));
		inventory.add(new InventoryItem(new ItemStack(Material.BED_BLOCK, 2), 3D, 30, "Some Lore Text"));
		inventory.add(new InventoryItem(new ItemStack(Material.BLAZE_POWDER, 2), 3D, 30, "Some Lore Text"));
		inventory.add(new InventoryItem(new ItemStack(Material.BOAT, 2), 3D, 30, "Some Lore Text"));
		inventory.add(new InventoryItem(new ItemStack(Material.BOOK, 2), 3D, 30, "Some Lore Text"));
		inventory.add(new InventoryItem(new ItemStack(Material.BOOK_AND_QUILL, 2), 3D, 30, "Some Lore Text"));
		inventory.add(new InventoryItem(new ItemStack(Material.BURNING_FURNACE, 2), 3D, 30, "Some Lore Text"));
		inventory.add(new InventoryItem(new ItemStack(Material.CARPET, 2), 3D, 30, "Some Lore Text"));
	}
	
	public List<InventoryItem> getInventory() {
		return inventory;
	}
}
