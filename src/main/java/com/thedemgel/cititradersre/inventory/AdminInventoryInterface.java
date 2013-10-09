
package com.thedemgel.cititradersre.inventory;

import com.thedemgel.cititradersre.inventory.annotation.InventoryPermission;
import com.thedemgel.cititradersre.inventory.annotation.InventoryTypeName;
import com.thedemgel.cititradersre.shop.Shop;
import com.thedemgel.cititradersre.util.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@InventoryPermission(Permissions.INVENTORY_ADMIN)
@InventoryTypeName("admin")
public class AdminInventoryInterface extends InventoryInterface {
	public static final int ADMIN_INVENTORY_STOCK = 128;

	public AdminInventoryInterface(Shop shop) {
		super(shop);
	}

	@Override
	public boolean hasItemInStock(ItemStack item) {
		return true;
	}

	@Override
	public boolean containsItem(ItemStack item) {
		return true;
	}

	@Override
	public void addInventory(ItemStack item) {
	}

	@Override
	public void removeInventory(ItemStack item, Integer amount) {
	}

	@Override
	public int getInventoryStock(ItemStack item) {
		return ADMIN_INVENTORY_STOCK;
	}

	@Override
	public boolean displayItemToPlayer(Player player) {
		return true;
	}

}
