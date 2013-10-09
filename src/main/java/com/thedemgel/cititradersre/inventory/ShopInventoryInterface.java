
package com.thedemgel.cititradersre.inventory;

import com.thedemgel.cititradersre.inventory.annotation.InventoryPermission;
import com.thedemgel.cititradersre.inventory.annotation.InventoryTypeName;
import com.thedemgel.cititradersre.shop.Shop;
import com.thedemgel.cititradersre.util.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@InventoryPermission(Permissions.INVENTORY_SHOP)
@InventoryTypeName("shop")
public class ShopInventoryInterface extends InventoryInterface {

	public ShopInventoryInterface(Shop shop) {
		super(shop);
	}

	@Override
	public boolean hasItemInStock(ItemStack item) {
		return true;
	}

	@Override
	public boolean containsItem(ItemStack item) {
		return getInventory().containsKey(item);
	}

	@Override
	public void addInventory(ItemStack item) {
		ItemStack invItem = item.clone();
		invItem.setAmount(1);

		if (getInventory().containsKey(invItem)) {
			int amount = getInventory().get(invItem).intValue() + item.getAmount();
			getInventory().put(invItem, amount);
		} else {
			getInventory().put(invItem, item.getAmount());
		}
	}

	@Override
	public void removeInventory(ItemStack item, Integer amount) {
		// Remove all Inventory
		if (amount == -1) {
			if (getInventory().containsKey(item)) {
				getInventory().remove(item);
			}
		} else {

			Integer currentInvAmount = getInventory().get(item);
			getInventory().put(item, currentInvAmount - amount);
		}
	}

	@Override
	public int getInventoryStock(ItemStack item) {
		if (getInventory().containsKey(item)) {
			return getInventory().get(item);
		}
		return 0;
	}

	@Override
	public boolean displayItemToPlayer(Player player) {
		return getShop().getOwner().equals(player.getName());
	}
}
