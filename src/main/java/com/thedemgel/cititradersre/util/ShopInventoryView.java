package com.thedemgel.cititradersre.util;

import com.thedemgel.cititradersre.shop.InventoryItem;
import com.thedemgel.cititradersre.shop.Shop;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopInventoryView extends InventoryView {

	private Inventory top;
	private Player player;
	private InventoryType type = InventoryType.CHEST;
	private boolean open = true;
	private List<Integer> reservedSlot = new ArrayList<>();
	private Shop shop;

	public ShopInventoryView(Inventory invTop, Inventory invBottom, Player player, Shop shop) {
		top = invTop;
		this.player = player;
		this.shop = shop;
		buildView();
	}

	public void setOpen() {
		open = false;
	}

	public boolean isOpen() {
		return open;
	}

	@Override
	public Inventory getTopInventory() {
		return top;
	}

	@Override
	public HumanEntity getPlayer() {
		return player;
	}

	@Override
	public InventoryType getType() {
		return type;
	}

	@Override
	public Inventory getBottomInventory() {
		return getPlayer().getInventory();
	}
	
	public List<Integer> getReservedSlots() {
		return reservedSlot;
	}
	
	public void buildView() {
		ItemStack arrow = new ItemStack(Material.ARROW, 1);
		ItemMeta arrowMeta = arrow.getItemMeta();
		List<String> arrowText = new ArrayList<>();
		arrowText.add("Click to go Back");
		arrowMeta.setLore(arrowText);
		arrowMeta.setDisplayName("Back");
		arrow.setItemMeta(arrowMeta);
		
		this.setItem(45, arrow);
		reservedSlot.add(45);
		
		for (InventoryItem item : shop.getInventory()) {
			this.getTopInventory().addItem(item.getItemStack());
		}
	}
}
