
package com.thedemgel.ultratrader.shop;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * Create an Item that is linked to a store.
 */
public class StoreItem {
	public static final Material STORE_ITEM_MATERIAL = Material.SPIDER_EYE;

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

	public static void linkToShop(Shop shop, ItemStack item) {
		List<String> lore = new ArrayList<>();
		lore.add("Use to view " + shop.getOwner() + "'s store.");
		lore.add("Shop Id: " + shop.getId());

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Store");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public static int getShopId(ItemStack item) {
		if (!item.hasItemMeta()) {
			return -1;
		}

		ItemMeta meta = item.getItemMeta();

		if (!meta.hasLore()) {
			return -1;
		}

		String id = Iterables.getLast(meta.getLore());

		if (id.startsWith("Shop Id: ")) {
			String idvalue = id.replaceFirst("Shop Id :", id);
			int idval = Integer.valueOf(idvalue);
			return idval;
		}

		return -1;
	}
}
