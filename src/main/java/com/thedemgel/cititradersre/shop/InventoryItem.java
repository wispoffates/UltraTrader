
package com.thedemgel.cititradersre.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class InventoryItem {
	private ItemStack itemStack;
	private Double sellprice;
	private Integer amount;
	private String random;
	private String description;

	public InventoryItem(ItemStack item, Double price, Integer amount, String lore) {
		itemStack = item.clone();
		sellprice = price;
		this.amount = amount;
		description = lore;
	}
	
	public ItemStack generateLore() {
		return generateLore(1);
	}
	
	/**
	 * Item Name
	 * Short Description (15 Characters)
	 * Price: 10.00
	 * Discount: Veteran
	 * 4d823Kd8
	 */
	public ItemStack generateLore(Integer stackAmount) {
		ItemStack genItem = itemStack.clone();
		List<String> genLore = new ArrayList<>();
		if (description.length() > 0) {
			genLore.add(description);
		}
		genLore.add("Price: " + (sellprice * stackAmount)); // Will need to figure for discounts
		// If there is a discount... add a message here
		// Id will always be LAST in the list
		genLore.add(ChatColor.DARK_GRAY + random);
		
		ItemMeta meta = genItem.getItemMeta();
		meta.setLore(genLore);
		genItem.setItemMeta(meta);
		
		genItem.setAmount(stackAmount);
		
		return genItem;
	}
	
	public String getRandomId() {
		return random;
	}
	
	public String setRandom() {
		random = RandomStringUtils.random(8, true, true);
		return random;
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
