package com.thedemgel.cititradersre.util;

import com.google.common.collect.Iterables;
import com.thedemgel.cititradersre.shop.InventoryItem;
import com.thedemgel.cititradersre.shop.Shop;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
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
	
	private Status current = Status.NULL;

	public ShopInventoryView(Inventory invTop, Inventory invBottom, Player player, Shop shop) {
		top = invTop;
		this.player = player;
		this.shop = shop;
		buildView();
	}

	public Status getStatus() {
		return current;
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
	
	public final void buildView() {
		current = Status.MAIN_SCREEN;
		top.clear();		
		for (InventoryItem item : shop.getInventory().values()) {
			this.getTopInventory().addItem(item.generateLore());
		}
	}
	
	public void buildItemView(ItemStack item) {
		current = Status.SELL_SCREEN;
		top.clear();
		//top.addItem(item);
		
		String id = Iterables.getLast(item.getItemMeta().getLore());
		id = id.substring(id.length() - 8);
		System.out.println(id);
		
		for (Entry<String, InventoryItem> inv : shop.getInventory().entrySet()) {
			System.out.println(inv.getKey());
		}
		
		InventoryItem invItem = shop.getInventory().get(id);
		
		
		Integer max = invItem.getItemStack().getMaxStackSize();
		Integer count = 1;
		while (count <= max && count <= invItem.getAmount()) {
			System.out.println("Count: " + count);
			top.addItem(invItem.generateLore(count));
			count = count * 2;
		}
		
		if ((count / 2) < invItem.getAmount() && (count / 2) < max) {
			System.out.println("Adding last full stack");
			top.addItem(invItem.generateLore(invItem.getAmount()));
		}
		
		ItemStack arrow = new ItemStack(Material.ARROW, 1);
		ItemMeta arrowMeta = arrow.getItemMeta();
		List<String> arrowText = new ArrayList<>();
		arrowText.add("Click to go Back");
		arrowMeta.setLore(arrowText);
		arrowMeta.setDisplayName("Back");
		arrow.setItemMeta(arrowMeta);
		
		this.setItem(45, arrow);
		reservedSlot.add(45);
	}
}
