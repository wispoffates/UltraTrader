
package com.thedemgel.ultratrader.shop;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * Create an Item that is linked to a store.
 */
public class StoreItem {
	
	public void createBlank(ItemStack item) {
		List<String> lore = new ArrayList<>();
		lore.add("Unassigned");
		lore.add("---");
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Store Item");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public void createLinkedToShop(Shop shop, ItemStack item) {
		linkToShop(shop, item);
	}
	
	public void linkToShop(Shop shop, ItemStack item) {
		List<String> lore = new ArrayList<>();
		lore.add("Use to view Tenowg's Store");
		lore.add("1");
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Store");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
}
