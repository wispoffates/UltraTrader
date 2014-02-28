package com.thedemgel.ultratrader.shop;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.UltraTrader;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemPrice {

	private ItemStack itemStack;
	private BigDecimal sellPrice;
    private BigDecimal buyPrice;
	private String random;
	private String description;
	private int slot = -1;
    private String categoryId = "";

	public ItemPrice() {
        sellPrice = BigDecimal.ZERO;
        buyPrice = BigDecimal.ZERO;
        description = "";
	}

	public ItemPrice(ItemStack item) {
		itemStack = item.clone();
		itemStack.setAmount(1);
		sellPrice = BigDecimal.ZERO;
        buyPrice = BigDecimal.ZERO;
		description = "";
	}

	public ItemPrice(ItemStack item, BigDecimal sellPrice2, BigDecimal buyPrice3, String lore) {
		itemStack = item.clone();
		itemStack.setAmount(1);
		sellPrice = sellPrice2;
        buyPrice = buyPrice3;
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
			case BUY_ITEM_SCREEN:
				genLore.add(ChatColor.GOLD + L.getFormatString("general.buypriceview", stackAmount, buyPrice, UltraTrader.getEconomy().format(buyPrice.multiply(BigDecimal.valueOf(stackAmount)).doubleValue())));
				break;
			case SELL_ITEM_SCREEN:
				genLore.add(ChatColor.BLUE + L.getFormatString("general.sellpriceview", stackAmount, sellPrice, UltraTrader.getEconomy().format(sellPrice.multiply(BigDecimal.valueOf(stackAmount)).doubleValue())));
				break;
			default:
                if (sellPrice.doubleValue() >= 0) {
                    genLore.add(ChatColor.BLUE + L.getString("general.left") + " " + L.getFormatString("general.sellprice", UltraTrader.getEconomy().format(sellPrice.multiply(BigDecimal.valueOf(stackAmount)).doubleValue())));
                }
                if (buyPrice.doubleValue() >= 0) {
                    genLore.add(ChatColor.GOLD + L.getString("general.right") + " " + L.getFormatString("general.buyprice", UltraTrader.getEconomy().format(buyPrice.multiply(BigDecimal.valueOf(stackAmount)).doubleValue())));
                }
		}
		// Will need to figure for discounts
		// If there is a discount... add a message here
		// Id will always be LAST in the list
		if (displayInventoryAmount) {
			genLore.add(ChatColor.GREEN + L.getString("general.instock") + ": " + stock);
		}
		genLore.add(ChatColor.BLACK + getId());

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

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal price) {
		this.sellPrice = price;
	}

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal price) {
        this.buyPrice = price;
    }

	/*public int getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}*/

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int rawslot) {
		slot = rawslot;
	}

    public void setCategoryId(String id) {
        this.categoryId = id;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
