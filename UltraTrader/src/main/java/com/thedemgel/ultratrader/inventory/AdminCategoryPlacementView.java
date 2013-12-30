package com.thedemgel.ultratrader.inventory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.thedemgel.ultratrader.InventoryHandler;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.shop.CategoryItem;
import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.shop.Status;
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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdminCategoryPlacementView extends InventoryView implements Listener {

	private final Player player;
	private final Shop shop;
	private final Inventory top;
	private final Status status;
	//private final ConcurrentMap<String, ItemPrice> items;
    private final Collection<CategoryItem> items;

	@SuppressWarnings("LeakingThisInConstructor")
	public AdminCategoryPlacementView(Player player, Shop shop, Status viewStatus) {
		this.player = player;
		this.shop = shop;
		this.top = Bukkit.createInventory(null, InventoryHandler.MAX_SELL_BUY_ITEMS, "Arrange your inventory.");
		status = viewStatus;

        items = shop.getCategoryItem().values();

		List<CategoryItem> itemQueue = new ArrayList<>();
		for (CategoryItem item : items) {
			if (item.getSlot() == -1) {
				itemQueue.add(item);
			} else {
				ItemStack slot = getTopInventory().getItem(item.getSlot());

				if (slot != null) {
					itemQueue.add(item);
				} else {
					getTopInventory().setItem(item.getSlot(), item.clone());
				}
			}
		}

		for (CategoryItem item : itemQueue) {
			int slot = getTopInventory().firstEmpty();
			item.setSlot(slot);
			this.getTopInventory().setItem(item.getSlot(), item.clone());
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

        System.out.println(event.getAction());

	}

	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {

		if (!event.getView().equals(this)) {
			return;
		}

		if (!event.getPlayer().getItemOnCursor().getType().equals(Material.AIR)) {
			player.setItemOnCursor(new ItemStack(Material.AIR));
			// Don't Update as item was in hand...
            // TODO: language addition
			player.sendMessage(ChatColor.RED + "Error: ItemOnCursor - Inventory not updated.");
		} else {
			// Update if everything is ok
			for (int i = 0; i < 36; i++) {
				ItemStack item = getTopInventory().getItem(i);
                System.out.println(item);
				if (item == null) {
					continue;
				}

				String id = CategoryItem.getCategoryId(item);
				CategoryItem categoryItem = shop.getCategoryItem().get(id);
				categoryItem.setSlot(i);
			}

			shop.save(true);
		}

		HandlerList.unregisterAll(this);
	}
}
