
package com.thedemgel.cititradersre.inventory;

import com.thedemgel.cititradersre.shop.ItemPrice;
import com.thedemgel.cititradersre.shop.Shop;
import org.bukkit.inventory.ItemStack;


public class ShopInventoryInterface extends InventoryInterface {

	public ShopInventoryInterface(Shop shop) {
		super(shop);
	}

	@Override
	public boolean hasItem(ItemStack item) {
		return true;
	}

	@Override
	public boolean hasItem(ItemPrice item) {
		return true;
	}
}
