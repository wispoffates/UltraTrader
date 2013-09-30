package com.thedemgel.cititradersre.inventory;

import com.thedemgel.cititradersre.shop.ItemPrice;
import com.thedemgel.cititradersre.shop.Shop;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public abstract class InventoryInterface {

	private Shop shop;
	private ConfigurationSection config;

	public InventoryInterface(Shop shop) {
		this.shop = shop;
		config = shop.getInventoryConfig();
	}

	public abstract boolean hasItem(ItemStack item);

	public abstract boolean hasItem(ItemPrice item);

	public void addInventory(ItemStack item) {
		ItemStack invItem = item.clone();
		invItem.setAmount(1);

		if (getInventory().containsKey(invItem)) {
			Integer amount = getInventory().get(invItem).intValue() + item.getAmount();
			getInventory().put(invItem, amount);
		} else {
			getInventory().put(invItem, item.getAmount());
		}
	}
	
	public void removeInventory(ItemStack item, Integer amount) {
		Integer currentInvAmount = getInventory().get(item);
		getInventory().put(item, currentInvAmount - amount);
	}
	
	public Integer getInventoryAmount(ItemStack item) {
		return getInventory().get(item);
	}
	
	public void getInventoryAmount(ItemPrice item) {
		
	}

	public ConcurrentMap<ItemStack, Integer> getInventory() {
		return shop.getInventory();
	}
}
