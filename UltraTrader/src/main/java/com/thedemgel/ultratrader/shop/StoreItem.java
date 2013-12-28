
package com.thedemgel.ultratrader.shop;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
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
		lore.add(ChatColor.GRAY + StoreItemType.STORE.name() + "UltraTrader");

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
		lore.add(ChatColor.YELLOW + "Use to view " + shop.getOwner() + "'s store.");
		lore.add(ChatColor.DARK_GRAY + "Shop Id: " + shop.getId());
		lore.add(ChatColor.BLACK + StoreItemType.STORE.name() + ":" + shop.getId() + ":UltraTrader");

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(shop.getName());
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public static String getItemShopId(ItemStack item) {
		String shopId = Iterables.getLast(item.getItemMeta().getLore());
		String[] parts = shopId.split(":");

		return parts[1];
	}

	public static boolean isUltraTraderItem(ItemStack item) {
		if (!item.hasItemMeta()) {
			return false;
		}

		if (!item.getItemMeta().hasLore()) {
			return false;
		}

		String id = Iterables.getLast(item.getItemMeta().getLore());
        try {
		    id = id.substring(id.length() - 11);
        } catch (StringIndexOutOfBoundsException ex) {
            return false;
        }

		if (id.equals("UltraTrader")) {
			return true;
		} else {
			return false;
		}
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
