
package com.thedemgel.cititradersre.shop;

import com.thedemgel.cititradersre.CitiTrader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class ItemPrice {
	private ItemStack itemStack;
	private BigDecimal price;
	private Integer amount;
	private String random;
	private String description;

	public ItemPrice() {};
	
	public ItemPrice(ItemStack item) {
		itemStack = item.clone();
		itemStack.setAmount(1);
		price = BigDecimal.ZERO;
		amount = 0;
		description = "";
	}
	
	public ItemPrice(ItemStack item, BigDecimal price, Integer amount, String lore) {
		itemStack = item.clone();
		itemStack.setAmount(1);
		this.price = price;
		this.amount = amount;
		description = lore;
	}
	
	public ItemStack generateLore() {
		return generateLore(1);
	}
	
	public ItemStack generateLore(Integer stackAmount) {
		return generateLore(stackAmount, false, 0);
	}
	/**
	 * Item Name
	 * Short Description (15 Characters)
	 * Price: 10.00
	 * Discount: Veteran
	 * 4d823Kd8
	 */
	public ItemStack generateLore(Integer stackAmount, boolean displayInventoryAmount, Integer stock) {
		ItemStack genItem = itemStack.clone();
		List<String> genLore = new ArrayList<>();
		if (getDescription().length() > 0) {
			genLore.add(getDescription());
		}
		genLore.add(ChatColor.GOLD + CitiTrader.getResourceBundle().getString("price") + ": " + price.multiply(BigDecimal.valueOf(stackAmount))); // Will need to figure for discounts
		// If there is a discount... add a message here
		// Id will always be LAST in the list
		if (displayInventoryAmount) {
			genLore.add(ChatColor.GREEN + "Stock: " + stock);
		}
		genLore.add(ChatColor.DARK_GRAY + getRandom());
		
		ItemMeta meta = genItem.getItemMeta();
		meta.setLore(genLore);
		genItem.setItemMeta(meta);
		
		genItem.setAmount(stackAmount);
		
		return genItem;
	}
	
	public String getRandomId() {
		return getRandom();
	}
	
	public String setRandom() {
		setRandom(RandomStringUtils.random(8, true, true));
		return getRandom();
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
