package com.thedemgel.cititradersre.shop;

import com.thedemgel.cititradersre.L;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
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
	private boolean keepAlive = false;
	private Shop shop;
	private Status current = Status.NULL;
	public Conversation convo;

	public ShopInventoryView(Inventory invTop, Player player, Shop shop) {
		top = invTop;
		this.player = player;
		this.shop = shop;
		buildView();
	}

	public Status getStatus() {
		return current;
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

	public final void buildView() {
		current = Status.MAIN_SCREEN;
		top.clear();
		boolean displayAdmin = shop.getInventoryInterface().displayItemToPlayer(player);
		for (ItemPrice item : getShop().getSellprices().values()) {
			int currentInvAmount = shop.getInventoryInterface().getInventoryStock(item);

			if (currentInvAmount > 0 || shop.isOwner(player)) {
				if (displayAdmin) {
					this.getTopInventory().addItem(item.generateLore(1, true, currentInvAmount));
				} else {
					this.getTopInventory().addItem(item.generateLore());
				}
			}
		}

		if (displayAdmin) {
			ItemStack doAdmin = new ItemStack(Material.BOOK_AND_QUILL);
			ItemMeta setPriceMeta = doAdmin.getItemMeta();
			List<String> doAdminText = new ArrayList<>();
			doAdminText.add(L.getString("inventory.admin.display"));
			setPriceMeta.setLore(doAdminText);
			setPriceMeta.setDisplayName(L.getString("inventory.admin.lore"));
			doAdmin.setItemMeta(setPriceMeta);
			this.setItem(53, doAdmin);
		}
	}

	public void buildItemView(ItemStack item) {
		current = Status.SELL_SCREEN;
		top.clear();

		String id = getShop().getItemId(item);

		ItemPrice invItem;
		if (getShop().getSellprices().containsKey(id)) {
			invItem = getShop().getSellprices().get(id);
		} else {
			buildView();
			return;
		}

		int invCount = 0;
		//if (shop.getInventory().containsKey(invItem.getItemStack())) {
		if (shop.getInventoryInterface().containsItem(invItem)) {
			invCount = shop.getInventoryInterface().getInventoryStock(invItem);
		} else {
			buildView();
			return;
		}

		if (invCount < 1 && !shop.getInventoryInterface().displayItemToPlayer(player)) {
			buildView();
			return;
		}

		Integer max = invItem.getItemStack().getMaxStackSize();
		Integer count = 1;
		while (count <= max && count <= invCount) {
			top.addItem(invItem.generateLore(count));
			count = count * 2;
		}

		if ((count / 2) < invCount && (count / 2) < max) {
			top.addItem(invItem.generateLore(invCount));
		}

		ItemStack arrow = new ItemStack(Material.ARROW);
		ItemMeta arrowMeta = arrow.getItemMeta();
		List<String> arrowText = new ArrayList<>();
		arrowText.add(L.getString("inventory.back.lore"));
		arrowMeta.setLore(arrowText);
		arrowMeta.setDisplayName(L.getString("inventory.back.display"));
		arrow.setItemMeta(arrowMeta);
		this.setItem(45, arrow);

		if (shop.isOwner(player)) {
			ItemStack setPrice = new ItemStack(Material.BOOK_AND_QUILL);
			ItemMeta setPriceMeta = setPrice.getItemMeta();
			List<String> setPriceText = new ArrayList<>();
			setPriceText.add(L.getString("inventory.itemadmin.lore"));
			setPriceText.add(ChatColor.DARK_GRAY + id);
			setPriceMeta.setLore(setPriceText);
			setPriceMeta.setDisplayName(L.getString("inventory.itemadmin.display"));
			setPrice.setItemMeta(setPriceMeta);
			this.setItem(53, setPrice);

			ItemStack removeStock;
			if (invCount > 0) {
				removeStock = new ItemStack(Material.WATER_BUCKET);
			} else {
				removeStock = new ItemStack(Material.BUCKET);
			}
			ItemMeta setRemoveStockMeta = removeStock.getItemMeta();
			List<String> setRemoveStockText = new ArrayList<>();
			if (invCount > 0) {
				setRemoveStockText.add(L.getString("inventory.removestock.lore.full"));
			} else {
				setRemoveStockText.add(L.getString("inventory.removestock.lore.empty"));
			}
			setRemoveStockText.add(ChatColor.DARK_GRAY + id);
			setRemoveStockMeta.setLore(setRemoveStockText);
			setRemoveStockMeta.setDisplayName(L.getString("inventory.removestock.display"));
			removeStock.setItemMeta(setRemoveStockMeta);
			this.setItem(50, removeStock);
		}
	}

	public Shop getShop() {
		return shop;
	}

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}
}
