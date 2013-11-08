package com.thedemgel.ultratrader.inventory;

import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.shop.Shop;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class InventoryInterface {

	private Shop shop;
	private ConcurrentMap<ItemStack, Integer> inv;

	public InventoryInterface(Shop shop) {
		this.shop = shop;
		inv = shop.getInventory();
	}

	/**
	 * Check to see if this item (single itemstack) is in the inventory.
	 * 
	 * @param item
	 * @return 
	 */
	public abstract boolean containsItem(ItemStack item);
	
	public final boolean containsItem(ItemPrice item) {
		return containsItem(item.getItemStack());
	}
	
	/**
	 * Checks to see if this item AND amount are in stock.
	 * 
	 * @param item
	 * @return 
	 */
	public abstract boolean hasItemInStock(ItemStack item);

	public final boolean hasItemInStock(ItemPrice item) {
		return hasItemInStock(item.getItemStack());
	}

	public abstract void addInventory(ItemStack item);
	
	public abstract void removeInventory(ItemStack item, Integer amount);
	
	/**
	 * Gets the current Stock level of an item, returns 0 if item isn't currently
	 * in inventory.
	 * @param item
	 * @return 
	 */
	public abstract int getInventoryStock(ItemStack item);
	
	public final int getInventoryStock(ItemPrice item) {
		return getInventoryStock(item.getItemStack());
	}

	protected ConcurrentMap<ItemStack, Integer> getInventory() {
		return inv;
	}
	
	/**
	 * Whether or not to display this item in the main inventory view of the
	 * trader.
	 * @return Boolean to display item.
	 */
	public abstract boolean displayItemToPlayer(Player player);
	
	public Shop getShop() {
		return shop;
	}
}
