
package com.thedemgel.cititradersre.shop;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class InventoryItem {
	private ItemStack itemStack;
	private Double sellprice;
	private Integer amount;

	public InventoryItem(ItemStack item, Double price, Integer amount, List<String> lore) {
		itemStack = item;
		sellprice = price;
		this.amount = amount;
		
		ItemMeta meta = item.getItemMeta();
		lore.add("Price: " + amount);
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	public InventoryItem(ItemStack item, Double price, Integer amount, String lore) {
		itemStack = item;
		sellprice = price;
		this.amount = amount;
		
		List<String> itemlore = new ArrayList<>();
		itemlore.add(lore);
		itemlore.add("Price: " + amount);
		ItemMeta meta = item.getItemMeta();
		meta.setLore(itemlore);
		item.setItemMeta(meta);
	}
	
	
	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public Double getSellprice() {
		return sellprice;
	}

	public void setSellprice(Double sellprice) {
		this.sellprice = sellprice;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	
}
