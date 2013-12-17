package com.thedemgel.ultratrader.inventory;

import com.thedemgel.ultratrader.InventoryHandler;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.shop.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class AdminItemPlacementView extends InventoryView implements Listener {

	private final Player player;
	private final Shop shop;
	private final Inventory top;
	private final Status status;
	private final ConcurrentMap<String, ItemPrice> items;

	@SuppressWarnings("LeakingThisInConstructor")
	public AdminItemPlacementView(Player player, Shop shop, Status viewstatus) {
		this.player = player;
		this.shop = shop;
		this.top = Bukkit.createInventory(null, InventoryHandler.MAX_SELL_BUY_ITEMS, "Arrange your inventory.");
		status = viewstatus;

		switch (status) {
			case MAIN_SCREEN:
				items = shop.getSellPrices();
				break;
			case BUY_SCREEN:
				items = shop.getBuyPrices();
				break;
			default:
				items = null;
		}

		List<ItemPrice> itemqueue = new ArrayList<>();
		for (ItemPrice item : items.values()) {
			if (item.getSlot() == -1) {
				itemqueue.add(item);
			} else {
				ItemStack slot = getTopInventory().getItem(item.getSlot());

				if (slot != null) {
					itemqueue.add(item);
				} else {
					getTopInventory().setItem(item.getSlot(), item.generateLore(1, status));
				}
			}
		}

		for (ItemPrice item : itemqueue) {
			int slot = getTopInventory().firstEmpty();
			item.setSlot(slot);
			this.getTopInventory().setItem(item.getSlot(), item.generateLore(1, status));
		}

		Bukkit.getPluginManager().registerEvents(this, UltraTrader.getInstance());
	}

	@Override
	public final Inventory getTopInventory() {
		return top;
	}

	@Override
	public Inventory getBottomInventory() {
		return player.getInventory();
	}

	@Override
	public HumanEntity getPlayer() {
		return player;
	}

	@Override
	public InventoryType getType() {
		return InventoryType.CHEST;
	}

	public Shop getShop() {
		return shop;
	}

	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent event) {
		if (!event.getView().equals(this)) {
			return;
		}

		event.setCancelled(true);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!event.getView().equals(this)) {
			return;
		}

		if (event.getRawSlot() >= InventoryHandler.MAX_SELL_BUY_ITEMS || event.getRawSlot() < 0) {
			event.setCancelled(true);
		}

	}

	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {

		if (!event.getView().equals(this)) {
			return;
		}

		if (!event.getPlayer().getItemOnCursor().getType().equals(Material.AIR)) {
			player.setItemOnCursor(new ItemStack(Material.AIR));
			// Don't Update as item was in hand...
			player.sendMessage(ChatColor.RED + "Error: ItemOnCursor - Inventory not updated.");
		} else {
			// Update if everything is ok
			//for (ItemStack item : getTopInventory()) {
			for (int i = 0; i < 36; i++) {
				ItemStack item = getTopInventory().getItem(i);

				if (item == null) {
					continue;
				}

				String id = shop.getItemId(item);
				ItemPrice itemprice = items.get(id);
				itemprice.setSlot(i);
			}

			shop.save();
		}

		HandlerList.unregisterAll(this);
	}
}
