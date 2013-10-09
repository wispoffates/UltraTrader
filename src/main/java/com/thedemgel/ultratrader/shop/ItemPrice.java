package com.thedemgel.ultratrader.shop;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.L;
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

	public ItemPrice() {
	}

	;

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

	public ItemStack generateLore(Status status) {
		return generateLore(1, status);
	}

	public ItemStack generateLore(Integer stackAmount, Status status) {
		return generateLore(stackAmount, false, 0, status);
	}

	/**
	 * Item Name Short Description (15 Characters) Price: 10.00 Discount:
	 * Veteran 4d823Kd8
	 */
	public ItemStack generateLore(Integer stackAmount, boolean displayInventoryAmount, Integer stock, Status status) {
		ItemStack genItem = itemStack.clone();

		List<String> genLore;
		if (genItem.hasItemMeta()) {
			if (genItem.getItemMeta().hasLore()) {
				genLore = genItem.getItemMeta().getLore();
			} else {
				genLore = new ArrayList<>();
			}
		} else {
			genLore = new ArrayList<>();
		}

		if (getDescription().length() > 0) {
			genLore.add(getDescription());
		}

		switch (status) {
			case BUY_SCREEN:
			case BUY_ITEM_SCREEN:
				genLore.add(ChatColor.GOLD + L.getFormatString("general.buyprice", UltraTrader.getEconomy().format(price.multiply(BigDecimal.valueOf(stackAmount)).doubleValue())));
				break;
			case MAIN_SCREEN:
			case SELL_SCREEN:
				genLore.add(ChatColor.BLUE + L.getFormatString("general.sellprice", UltraTrader.getEconomy().format(price.multiply(BigDecimal.valueOf(stackAmount)).doubleValue())));
				break;
			default:
		}
		//genLore.add(ChatColor.GOLD + L.getString("general.price") + ": " + ); // Will need to figure for discounts
		// If there is a discount... add a message here
		// Id will always be LAST in the list
		if (displayInventoryAmount) {
			genLore.add(ChatColor.GREEN + L.getString("general.instock") + ": " + stock);
		}
		genLore.add(ChatColor.DARK_GRAY + getId());

		ItemMeta meta = genItem.getItemMeta();
		meta.setLore(genLore);
		genItem.setItemMeta(meta);

		genItem.setAmount(stackAmount);

		return genItem;
	}

	public String getId() {
		return random;
	}

	public String setRandom() {
		setRandom(RandomStringUtils.random(8, true, true));
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
